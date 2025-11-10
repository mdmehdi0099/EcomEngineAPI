package com.Ecom.EcomEngineAPI.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {
    private Long userId;
    private Long addressId;
    private List<Long> cartItemIds; // used if order placed from cart
    private Long productId;         // used if order placed directly
    private Long quantity;
    private String paymentMode;


}
