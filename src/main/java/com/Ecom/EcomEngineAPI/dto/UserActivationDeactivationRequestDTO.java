package com.Ecom.EcomEngineAPI.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivationDeactivationRequestDTO {
    private String email;
    private boolean active;
    private String deactivationReason;
    private String reActivationReason;
}
