package com.Ecom.EcomEngineAPI.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private String description;
    private Long price;
    private String pictureUrl;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private boolean active;
    private boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long inventoryCount;
    private Long stockKeepingUnit;


}
