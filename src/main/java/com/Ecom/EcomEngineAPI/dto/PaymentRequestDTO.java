package com.Ecom.EcomEngineAPI.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    private Long orderId;
    private Long userId;
    private String paymentMode;   // CARD, UPI, COD, WALLET
    private Long amount;
    private String transactionNote;

}
