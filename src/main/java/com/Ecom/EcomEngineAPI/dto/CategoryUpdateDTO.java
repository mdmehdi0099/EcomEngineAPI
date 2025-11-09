package com.Ecom.EcomEngineAPI.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateDTO {
    private String name;
    private String description;
    private boolean active;
    private String deactivationReason;
    private LocalDateTime deactivatedAt;
    private String reactivationReason;
    private LocalDateTime reactivatedAt;
}
