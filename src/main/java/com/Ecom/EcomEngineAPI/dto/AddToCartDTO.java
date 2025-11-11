package com.Ecom.EcomEngineAPI.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartDTO {
    private Long productId;
    private Long quantity;

}
