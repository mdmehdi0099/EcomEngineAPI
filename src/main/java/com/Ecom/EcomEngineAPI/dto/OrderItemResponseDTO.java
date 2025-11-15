package com.Ecom.EcomEngineAPI.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponseDTO {
    private Long orderItemId;
    private Long productId;
    private String productName;
    private Long price;
    private Long quantity;
    private Long totalPrice;
}
