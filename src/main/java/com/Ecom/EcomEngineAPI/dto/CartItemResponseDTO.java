package com.Ecom.EcomEngineAPI.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO {
    private Long productId;
    private String productName;
    private Long pricePerUnit;
    private Long quantity;
    private Long totalPrice;

}
