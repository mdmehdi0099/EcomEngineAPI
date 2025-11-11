package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.AuthResponse;
import com.Ecom.EcomEngineAPI.dto.LoginRequest;
import com.Ecom.EcomEngineAPI.dto.RegisterRequest;
import com.Ecom.EcomEngineAPI.entity.Role;
import com.Ecom.EcomEngineAPI.entity.User;
import com.Ecom.EcomEngineAPI.repo.UserRepository;
import com.Ecom.EcomEngineAPI.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public ResponseEntity<AuthResponse> register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            AuthResponse response = AuthResponse.builder()
                    .username(req.getUsername())
                    .email(req.getEmail())
                    .phone(req.getPhone())
                    .role(String.valueOf(req.getRole()))       // ✅ Added here
                    .message("User already exists")
                    .isActive(true)
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        Role role = req.getRole() != null ? req.getRole() : Role.CUSTOMER;
        if (!role.equals("CUSTOMER")) {
            role = Role.CUSTOMER;
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(role);
        user.setEmailVerified(false);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setPhoneVerified(false);

        User savedUser = userRepository.save(user);

        User u = userRepository.findByEmail(req.getEmail());

        // Build claims to include requested fields
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", u.getUserId());
        claims.put("username", u.getUsername());
        claims.put("email", u.getEmail());
        claims.put("phone", u.getPhone());
        claims.put("role", u.getRole().name());
        claims.put("isActive", u.isActive());

        String token = jwtHelper.generateToken(claims, u.getEmail());
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .userId(u.getUserId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(String.valueOf(u.getRole()))       // ✅ Added here
                .isActive(u.isActive())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<AuthResponse> registerAdminUser(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            AuthResponse response = AuthResponse.builder()
                    .username(req.getUsername())
                    .email(req.getEmail())
                    .phone(req.getPhone())
                    .role(String.valueOf(req.getRole()))       // ✅ Added here
                    .message("User already exists")
                    .isActive(true)
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Role role = req.getRole() != null ? req.getRole() : Role.ADMIN;
        if (!role.equals("ADMIN")) {
            role = Role.ADMIN;
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(role);
        user.setEmailVerified(false);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setPhoneVerified(false);

        User savedUser = userRepository.save(user);


        User u = userRepository.findByEmail(req.getEmail());

        // Build claims to include requested fields
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", u.getUserId());
        claims.put("username", u.getUsername());
        claims.put("email", u.getEmail());
        claims.put("phone", u.getPhone());
        claims.put("role", u.getRole().name());
        claims.put("isActive", u.isActive());

        String token = jwtHelper.generateToken(claims, u.getEmail());
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .userId(u.getUserId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(String.valueOf(u.getRole()))       // ✅ Added here
                .isActive(u.isActive())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<AuthResponse> registerSellerUser(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            AuthResponse response = AuthResponse.builder()
                    .username(req.getUsername())
                    .email(req.getEmail())
                    .phone(req.getPhone())
                    .role(String.valueOf(req.getRole()))       // ✅ Added here
                    .message("User already exists")
                    .isActive(true)
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Role role = req.getRole() != null ? req.getRole() : Role.SELLER;
        if (!role.equals("SELLER")) {
            role = Role.SELLER;
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(role);
        user.setEmailVerified(false);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setPhoneVerified(false);

        User savedUser = userRepository.save(user);


        User u = userRepository.findByEmail(req.getEmail());

        // Build claims to include requested fields
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", u.getUserId());
        claims.put("username", u.getUsername());
        claims.put("email", u.getEmail());
        claims.put("phone", u.getPhone());
        claims.put("role", u.getRole().name());
        claims.put("isActive", u.isActive());

        String token = jwtHelper.generateToken(claims, u.getEmail());
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .userId(u.getUserId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(String.valueOf(u.getRole()))       // ✅ Added here
                .isActive(u.isActive())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<AuthResponse> registerAgentUser(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            AuthResponse response =
                    AuthResponse.builder()
                            .username(req.getUsername())
                            .email(req.getEmail())
                            .phone(req.getPhone())
                            .role(String.valueOf(req.getRole()))       // ✅ Added here
                            .message("User already exists")
                            .isActive(true)
                            .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Role role = req.getRole() != null ? req.getRole() : Role.AGENT;
        if (!role.equals("AGENT")) {
            role = Role.AGENT;
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(role);
        user.setEmailVerified(false);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setPhoneVerified(false);

        User savedUser = userRepository.save(user);

        User u = userRepository.findByEmail(req.getEmail());

        // Build claims to include requested fields
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", u.getUserId());
        claims.put("username", u.getUsername());
        claims.put("email", u.getEmail());
        claims.put("phone", u.getPhone());
        claims.put("role", u.getRole().name());
        claims.put("isActive", u.isActive());

        String token = jwtHelper.generateToken(claims, u.getEmail());
        AuthResponse response1 = AuthResponse.builder()
                .token(token)
                .userId(u.getUserId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(String.valueOf(u.getRole()))       // ✅ Added here
                .isActive(u.isActive())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response1);
    }

    public ResponseEntity<AuthResponse> login(LoginRequest req) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(req.getEmail()));
        if (optionalUser.isEmpty()) {
            AuthResponse response = AuthResponse.builder()
                    .email(req.getEmail())
                    .message("User not found")
                    .token(null)
                    .userId(null)
                    .username(null)
                    .phone(null)
                    .role(null)
                    .isActive(false)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        User user = userRepository.findByEmail(req.getEmail());

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            AuthResponse response1 = AuthResponse.builder()
                    .email(req.getEmail())
                    .message("Invalid Credential")
                    .token(null)
                    .userId(null)
                    .username(null)
                    .phone(null)
                    .role(null)
                    .isActive(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response1);
        }

        if (!user.isActive()) {
            AuthResponse response2 = AuthResponse.builder()
                    .email(req.getEmail())
                    .message("User Inactive. Contact Admin")
                    .token(null)
                    .userId(null)
                    .username(null)
                    .phone(null)
                    .role(null)
                    .isActive(false)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response2);
        }
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // Build claims to include requested fields
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("phone", user.getPhone());
        claims.put("role", user.getRole().name());
        claims.put("isActive", user.isActive());

        String token = jwtHelper.generateToken(claims, user.getEmail());
        AuthResponse response3 = AuthResponse.builder()
                .token(token)
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(String.valueOf(user.getRole()))       // ✅ Added here
                .isActive(user.isActive())
                .message("Logged in successfully...")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response3);
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
