package com.Ecom.EcomEngineAPI.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandCreateDTO {
    private String name;
    private String description;
    private String logoUrl;
    private String websiteUrl;
}
