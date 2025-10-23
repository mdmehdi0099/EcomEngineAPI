package com.Ecom.EcomEngineAPI.controller;

import com.Ecom.EcomEngineAPI.dto.*;
import com.Ecom.EcomEngineAPI.service.AuthService;
import com.Ecom.EcomEngineAPI.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        AuthResponse res = authService.register(req);
        return ResponseEntity.ok(res);
    }
    //registerAdminUser
    @PostMapping("/registerAdminUser")
    public ResponseEntity<?> registerAdminUser(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerAdminUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        AuthResponse res = authService.login(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<ForgetPasswordResponseDTO> forgetPassword(@RequestBody ForgetPasswordRequestDTO forgetPasswordRequestDTO) {
        String email = forgetPasswordRequestDTO.getEmail();
        ForgetPasswordResponseDTO response = emailService.sendForgetPasswordEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ResetPasswordResponseDTO> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
        ResetPasswordResponseDTO response = emailService.resetPassword(resetPasswordRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @PostMapping("/refreshToken")
//    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
//        String h = request.getHeader("Authorization");
//        if (h == null || !h.startsWith("Bearer ")) return ResponseEntity.badRequest().body("Missing Authorization header");
//        String oldToken = h.substring(7);
//        AuthResponse res = authService.refresh(oldToken);
//        return ResponseEntity.ok(res);
//    }




}
