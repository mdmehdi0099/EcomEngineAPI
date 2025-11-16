package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.*;
import com.Ecom.EcomEngineAPI.entity.*;
import com.Ecom.EcomEngineAPI.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;


    @Transactional
    public Order placeOrder(OrderRequestDTO dto) {
        // ✅ Step 1: Fetch Address
        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        List<OrderItem> orderItems = new ArrayList<>();
        long totalAmount = 0L;

        // ✅ Step 2: If order is from Cart
        if (dto.getCartItemIds() != null && !dto.getCartItemIds().isEmpty()) {
            // Fetch only items belonging to this user's cart
            List<CartItem> cartItems = cartItemRepository.findAllById(dto.getCartItemIds())
                    .stream()
                    .filter(ci -> ci.getCart() != null && ci.getCart().getUserId().equals(dto.getUserId()))
                    .toList();

            if (cartItems.isEmpty()) {
                throw new RuntimeException("No valid cart items found for this user");
            }

            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                long subtotal = product.getPrice() * cartItem.getQuantity();

                orderItems.add(OrderItem.builder()
                        .product(product)
                        .quantity(cartItem.getQuantity())
                        .price(product.getPrice())
                        .totalPrice(subtotal)// ✅ price is total price for this item
                        .build());

                totalAmount += subtotal;
            }

            // Remove purchased items from cart
            cartItemRepository.deleteAll(cartItems);
            cartRepository.flush();
        }
        // ✅ Step 3: Direct product order
        else if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            long subtotal = product.getPrice() * dto.getQuantity();

            orderItems.add(OrderItem.builder()
                    .product(product)
                    .quantity(dto.getQuantity())
                    .price(product.getPrice())
                    .totalPrice(subtotal)
                    .build());
            totalAmount = subtotal;
        }
        else {
            throw new RuntimeException("No product or cart items provided for order");
        }

        // ✅ Step 4: Create Order (unpaid at this stage)
        Order order = Order.builder()
                .userId(dto.getUserId())
                .shippingAddress(address)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .items(orderItems)
                .paid(false)
                .build();

        // Set back-reference for JPA
        orderItems.forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);
        // Step 4: Call PaymentService to process payment
        PaymentRequestDTO paymentRequest = PaymentRequestDTO.builder()
                .orderId(savedOrder.getOrderId())
                .userId(dto.getUserId())
                .paymentMode(dto.getPaymentMode())
                .amount(totalAmount)
                .transactionNote("Order Payment")
                .build();
        PaymentResponseDTO paymentResponse = paymentService.processPayment(paymentRequest);
        // Step 5: Fetch saved payment from DB
        Payment payment = paymentRepository.findById(paymentResponse.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment record not found"));

        // ✅ Step 6: Update order with payment result
        savedOrder.setPayment(payment);
        savedOrder.setPaid(payment.getStatus() == PaymentStatus.SUCCESS);
        savedOrder.setStatus(
                payment.getStatus() == PaymentStatus.SUCCESS
                        ? OrderStatus.CONFIRMED
                        : OrderStatus.PENDING
        );

        // ✅ Step 7: Update user's cart totals
        cartRepository.findByUserId(dto.getUserId()).ifPresent(cart -> {
            long newTotal = cart.getCartItems().stream()
                    .mapToLong(ci -> ci.getProduct().getPrice() * ci.getQuantity())
                    .sum();
            cart.setTotalAmount(newTotal);
            cart.setTotalItems((long) cart.getCartItems().size());
            cartRepository.save(cart);
        });

        // ✅ Step 8: Save and return final order
        return orderRepository.save(savedOrder);
    }

    // ------------------- Retrieval methods ------------------- //
    public OrderResponseDTO convertToDTO(Order order){
        List<OrderItemResponseDTO> itemDTOs=order.getItems().stream().map(item->OrderItemResponseDTO.builder()
                .orderItemId(item.getOrderItemId())
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .totalPrice(item.getPrice()*item.getQuantity())
                .build()
            ).toList();
        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .shippingAddress(order.getShippingAddress())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .paid(order.isPaid())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(itemDTOs)
                .build();
    }
    public List<OrderResponseDTO> getOrdersByUserDTO(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    //cancel order
    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId) {

        // 1️⃣ Fetch order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Validate if order can be cancelled
        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed orders can be cancelled.");
        }

        if (order.isPaid() == false) {
            throw new RuntimeException("Cannot cancel unpaid order.");
        }

        // 3️⃣ Update order status
        order.setStatus(OrderStatus.CANCELLED);
        order.setPaid(false);
        order.setCancelledAt(LocalDateTime.now());

        // 4️⃣ Optional: Update payment status if needed
        if (order.getPayment() != null) {
            Payment payment = order.getPayment();
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }

        Order saved = orderRepository.save(order);

        // 5️⃣ Convert to response DTO
        return convertToDTO(saved);
    }




}
