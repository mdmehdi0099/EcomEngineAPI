package com.Ecom.EcomEngineAPI.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivationDeactivationRequestDTO {
    private String email;
    private boolean active;
}
