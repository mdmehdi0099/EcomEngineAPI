package com.Ecom.EcomEngineAPI.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false, unique = true)
    private String paymentReference;

    @Column(nullable = false, unique = true)
    private String orderReference; // renamed to avoid confusion with Order entity

    private Long userId;

    @Column(nullable = false)
    private String paymentMode; // e.g., "UPI", "CREDIT_CARD", "DEBIT_CARD"

    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String transactionNote;

    @CreatedDate
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ✅ Bidirectional link back to Order
    @OneToOne(mappedBy = "payment")
    private Order order;






}
