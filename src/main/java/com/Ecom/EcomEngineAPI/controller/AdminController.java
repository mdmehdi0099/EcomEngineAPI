package com.Ecom.EcomEngineAPI.controller;

import com.Ecom.EcomEngineAPI.dto.AuthResponse;
import com.Ecom.EcomEngineAPI.dto.RegisterRequest;
import com.Ecom.EcomEngineAPI.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AuthService authService;

    //registerSellerUser
    @PostMapping("/registerSellerUser")
    public ResponseEntity<?> registerSellerUser(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerSellerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //registerAgentUser
    @PostMapping("/registerSellerUser")
    public ResponseEntity<?> registerAgentUser(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerAgentUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
