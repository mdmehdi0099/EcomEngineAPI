package com.Ecom.EcomEngineAPI.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {
    private Long cartId;
    private Long userId;
    private Long totalItems;
    private Long totalAmount;
    private List<CartItemResponseDTO> items;

}
