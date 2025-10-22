package com.Ecom.EcomEngineAPI.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String role;// ✅ Added role
    private String message;
    private boolean isActive;
}
