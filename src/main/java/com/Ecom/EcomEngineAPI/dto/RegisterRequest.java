package com.Ecom.EcomEngineAPI.dto;

import com.Ecom.EcomEngineAPI.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String username;
    private String email;
    private String phone;
    private String password;
    private Role role;
}
