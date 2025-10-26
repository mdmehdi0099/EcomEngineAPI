package com.Ecom.EcomEngineAPI.dto;


import com.Ecom.EcomEngineAPI.entity.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private Role role;
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    private boolean isActive;
    private String deactivationReason;
    private LocalDateTime deactivatedAt;
    private String reActivationReason;
    private LocalDateTime reActivatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private String message;

}
