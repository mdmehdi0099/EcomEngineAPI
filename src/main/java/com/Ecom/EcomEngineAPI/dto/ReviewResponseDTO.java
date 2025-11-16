package com.Ecom.EcomEngineAPI.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDTO {
    private Long reviewId;
    private Long productId;
    private Long orderId;
    private Long userId;
    private Long starRate;
    private String remark;
    private String other;
    private boolean enable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
