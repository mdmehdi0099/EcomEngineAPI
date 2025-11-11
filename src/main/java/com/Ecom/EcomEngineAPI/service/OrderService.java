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


    @Transactional
    public Order placeOrder(OrderRequestDTO dto) {
        // Step 1: Fetch Address
        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        List<OrderItem> orderItems = new ArrayList<>();
        Long totalAmount = 0L;

        // Step 2: If order is from Cart
        if (dto.getCartItemIds() != null && !dto.getCartItemIds().isEmpty()) {
            List<CartItem> cartItems = cartItemRepository.findAllById(dto.getCartItemIds());
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                Long subtotal = product.getPrice() * cartItem.getQuantity();

                orderItems.add(OrderItem.builder()
                        .product(product)
                        .quantity(cartItem.getQuantity())
                        .price(subtotal)
                        .build());

                totalAmount += subtotal;
            }

            // Remove purchased items from cart after successful order
            cartItemRepository.deleteAll(cartItems);

        } else if (dto.getProductId() != null) {
            // Step 3: Direct product order
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            Long subtotal = product.getPrice() * dto.getQuantity();

            orderItems.add(OrderItem.builder()
                    .product(product)
                    .quantity(dto.getQuantity())
                    .price(subtotal)
                    .build());

            totalAmount = subtotal;
        }

        // Step 4: Create Order
        Order order = Order.builder()
                .userId(dto.getUserId())
                .shippingAddress(address)
                .status(OrderStatus.CONFIRMED)
                .totalAmount(totalAmount)
                .paid(true)
                .build();

        order.setItems(orderItems);
        orderItems.forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);

        // Step 5: Create Mock Payment
        Payment payment = Payment.builder()
                .orderReference(String.valueOf(savedOrder.getOrderId())) // ✅ updated field name
                .userId(dto.getUserId())
                .amount(totalAmount)
                .paymentMode(dto.getPaymentMode())
                .paymentReference(UUID.randomUUID().toString())
                .status(PaymentStatus.SUCCESS)
                .transactionNote("Mock payment success")
                .build();

        paymentRepository.save(payment);
        savedOrder.setPayment(payment);

        // Step 6: Update Cart Total if any cart remains
        Cart userCart = cartRepository.findByUserId(dto.getUserId()).orElse(null);
        if (userCart != null) {
            Long newTotal = userCart.getCartItems().stream()
                    .mapToLong(ci -> ci.getProduct().getPrice() * ci.getQuantity())
                    .sum();
            userCart.setTotalAmount(newTotal);
            cartRepository.save(userCart);
        }

        return orderRepository.save(savedOrder);
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

}
