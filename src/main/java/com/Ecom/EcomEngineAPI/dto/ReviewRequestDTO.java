package com.Ecom.EcomEngineAPI.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequestDTO {

    private Long productId;
    private Long orderId;
    private Long userId;
    private Long starRate;
    private String remark;
    private String other;

}
