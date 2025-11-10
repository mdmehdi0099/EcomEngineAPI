package com.Ecom.EcomEngineAPI.dto;


import com.Ecom.EcomEngineAPI.entity.PaymentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Long paymentId;
    private String paymentReference;
    private Long orderId;
    private Long userId;
    private String paymentMode;
    private Long amount;
    private PaymentStatus status;
    private String transactionNote;

}
