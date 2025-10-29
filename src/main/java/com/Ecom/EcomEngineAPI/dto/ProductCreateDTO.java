package com.Ecom.EcomEngineAPI.dto;


import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDTO {
    private String name;
    private String description;
    private Long price;
    private String pictureUrl;
    private Long brandId;
    private Long categoryId;
    private Long stockKeepingUnit;
    private Long inventoryCount;



}
