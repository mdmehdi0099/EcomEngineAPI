package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.OrderRequestDTO;
import com.Ecom.EcomEngineAPI.entity.*;
import com.Ecom.EcomEngineAPI.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            List<CartItem> cartItems = cartItemRepository.findAllById(dto.getCartItemIds());

            if (cartItems.isEmpty()) {
                throw new RuntimeException("No valid cart items found for this user");
            }

            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                long subtotal = product.getPrice() * cartItem.getQuantity();

                orderItems.add(OrderItem.builder()
                        .product(product)
                        .quantity(cartItem.getQuantity())
                        .price(subtotal) // ✅ price is total price for this item
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
                    .price(subtotal)
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

        // Step 5: Create Mock Payment
         Payment payment = Payment.builder()
         .orderReference(savedOrder.getOrderId() + "-" + UUID.randomUUID()) // ✅ updated field name
         .userId(dto.getUserId())
         .amount(totalAmount)
         .paymentMode(dto.getPaymentMode())
         .paymentReference(UUID.randomUUID().toString())
         .status(PaymentStatus.SUCCESS)
         .transactionNote("Mock payment success")
         .build();

         paymentRepository.save(payment);

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

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}
