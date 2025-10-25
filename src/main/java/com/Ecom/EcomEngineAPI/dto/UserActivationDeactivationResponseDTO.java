package com.Ecom.EcomEngineAPI.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivationDeactivationResponseDTO {
    private String email;
    private boolean active;
    private String message;
}
