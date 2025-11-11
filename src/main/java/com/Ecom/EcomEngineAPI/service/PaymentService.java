package com.Ecom.EcomEngineAPI.service;


import com.Ecom.EcomEngineAPI.dto.PaymentRequestDTO;
import com.Ecom.EcomEngineAPI.dto.PaymentResponseDTO;
import com.Ecom.EcomEngineAPI.entity.Order;
import com.Ecom.EcomEngineAPI.entity.OrderStatus;
import com.Ecom.EcomEngineAPI.entity.Payment;
import com.Ecom.EcomEngineAPI.entity.PaymentStatus;
import com.Ecom.EcomEngineAPI.repo.OrderRepository;
import com.Ecom.EcomEngineAPI.repo.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {
        // 1️⃣ Fetch order
//        Order order = orderRepository.findById(dto.getOrderId())
//                .orElseThrow(() -> new RuntimeException("Order not found"));


        // 2️⃣ Mock payment processing
        PaymentStatus mockStatus = mockPaymentProcessing(dto.getPaymentMode());

        // 3️⃣ Create payment record
        Payment payment = Payment.builder()
                .paymentReference(UUID.randomUUID().toString())
                .orderReference(String.valueOf(dto.getOrderId()))
                .userId(dto.getUserId())
                .paymentMode(dto.getPaymentMode())
                .amount(dto.getAmount())
                .status(mockStatus)
                .transactionNote(dto.getTransactionNote())
                .createdAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // 4️⃣ Update order if payment success
        if (mockStatus == PaymentStatus.SUCCESS) {

//            order.setPaid(true);
//            order.setStatus(OrderStatus.CONFIRMED);
//            orderRepository.save(order);

        } else if (mockStatus == PaymentStatus.FAILED) {

            //order.setStatus(OrderStatus.PENDING);

        }

        // 5️⃣ Response
        return PaymentResponseDTO.builder()
                .paymentId(savedPayment.getPaymentId())
                .paymentReference(savedPayment.getPaymentReference())
                .orderId(dto.getOrderId())
                .userId(savedPayment.getUserId())
                .paymentMode(savedPayment.getPaymentMode())
                .amount(savedPayment.getAmount())
                .status(savedPayment.getStatus())
                .transactionNote(savedPayment.getTransactionNote())
                .build();
    }

    // Mock payment simulation logic
    private PaymentStatus mockPaymentProcessing(String mode) {
        // Simulate random success/failure (you can make it deterministic if you like)
        return Math.random() > 0.2 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
    }


    //payment list
    public List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByStatus(status);

        return payments.stream()
                .map(payment -> PaymentResponseDTO.builder()
                        .paymentId(payment.getPaymentId())
                        .paymentReference(payment.getPaymentReference())
                        .orderId(Long.valueOf(payment.getOrderReference()))
                        .userId(payment.getUserId())
                        .paymentMode(payment.getPaymentMode())
                        .amount(payment.getAmount())
                        .status(payment.getStatus())
                        .transactionNote(payment.getTransactionNote())
                        .build())
                .collect(Collectors.toList());
    }





}
