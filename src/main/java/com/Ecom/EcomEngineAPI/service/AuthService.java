package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.AuthResponse;
import com.Ecom.EcomEngineAPI.dto.LoginRequest;
import com.Ecom.EcomEngineAPI.dto.RegisterRequest;
import com.Ecom.EcomEngineAPI.entity.Role;
import com.Ecom.EcomEngineAPI.entity.User;
import com.Ecom.EcomEngineAPI.repo.UserRepository;
import com.Ecom.EcomEngineAPI.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return AuthResponse.builder()
                    .username(req.getUsername())
                    .email(req.getEmail())
                    .phone(req.getPhone())
                    .role(String.valueOf(req.getRole()))       // ✅ Added here
                    .message("User already exists")
                    .isActive(true)
                    .build();
        }

        Role role = req.getRole() != null ? req.getRole() : Role.USER;

        User u = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .phone(req.getPhone())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .isEmailVerified(false)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(u);
        return AuthResponse.builder()
                .userId(u.getUserId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(String.valueOf(u.getRole()))       // ✅ Added here
                .isActive(u.isActive())
                .message("User registration successful")
                .build();
    }
    public AuthResponse registerAdminUser(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return AuthResponse.builder()
                    .username(req.getUsername())
                    .email(req.getEmail())
                    .phone(req.getPhone())
                    .role(String.valueOf(req.getRole()))       // ✅ Added here
                    .message("User already exists")
                    .isActive(true)
                    .build();
        }
        //Role role = req.getRole() != null ? req.getRole() : Role.USER;
        User u = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .phone(req.getPhone())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.valueOf("ADMIN"))
                .isEmailVerified(false)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(u);
        return AuthResponse.builder()
                .userId(u.getUserId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(String.valueOf(u.getRole()))       // ✅ Added here
                .isActive(u.isActive())
                .message("Admin user registration successful")
                .build();
    }



    public AuthResponse login(LoginRequest req) {
        User u = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!u.isActive()) {
            throw new RuntimeException("Account inactive. Contact admin.");
        }
        u.setLastLoginAt(LocalDateTime.now());
        userRepository.save(u);

        // Build claims to include requested fields
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", u.getUserId());
        claims.put("username", u.getUsername());
        claims.put("email", u.getEmail());
        claims.put("phone", u.getPhone());
        claims.put("role", u.getRole().name());
        claims.put("isActive", u.isActive());

        String token = jwtHelper.generateToken(claims, u.getEmail());
        return AuthResponse.builder()
                .token(token)
                .userId(u.getUserId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(String.valueOf(u.getRole()))       // ✅ Added here
                .isActive(u.isActive())
                .build();
    }
//    public AuthResponse refresh(String oldToken) {
//        if (!jwtHelper.validateToken(oldToken)) {
//            throw new RuntimeException("Invalid/expired token");
//        }
//        String newToken = jwtHelper.refreshToken(oldToken);
//        return AuthResponse.builder()
//                .token(newToken)
//                .userId(u.getUserId())
//                .username(u.getUsername())
//                .email(u.getEmail())
//                .phone(u.getPhone())
//                .role(String.valueOf(u.getRole()))       // ✅ Added here
//                .isActive(u.isActive())
//                .build();
//    }


}
