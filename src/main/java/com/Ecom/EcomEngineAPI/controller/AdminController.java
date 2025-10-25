package com.Ecom.EcomEngineAPI.controller;

import com.Ecom.EcomEngineAPI.dto.AuthResponse;
import com.Ecom.EcomEngineAPI.dto.RegisterRequest;
import com.Ecom.EcomEngineAPI.dto.UserActivationDeactivationRequestDTO;
import com.Ecom.EcomEngineAPI.service.AdminService;
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

    @Autowired
    private AdminService adminService;

    //registerSellerUser
    @PostMapping("/registerSellerUser")
    public ResponseEntity<?> registerSellerUser(@RequestBody RegisterRequest request) {
        return authService.registerSellerUser(request);
    }

    //registerAgentUser
    @PostMapping("/registerAgentUser")
    public ResponseEntity<?> registerAgentUser(@RequestBody RegisterRequest request) {
        return authService.registerAgentUser(request);
    }

    //deactivate user by email
    @PostMapping("/deactivateAgentUser")
    public ResponseEntity<?> deactivateAgentUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateAgentUser(request);
    }

    //reActivate Agent User by email
    @PostMapping("/reActivateAgentUser")
    public ResponseEntity<?> reActivateAgentUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateAgentUser(request);
    }

    //deactivate user by email
    @PostMapping("/deactivateSellerUser")
    public ResponseEntity<?> deactivateSellerUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateSellerUser(request);
    }

    //deactivate user by email
    @PostMapping("/reActivateSellerUser")
    public ResponseEntity<?> reActivateSellerUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateSellerUser(request);
    }

    //deactivate user by email
    @PostMapping("/deactivateCustomer")
    public ResponseEntity<?> deactivateCustomer(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateCustomer(request);
    }

    //deactivate user by email
    @PostMapping("/reActivateCustomer")
    public ResponseEntity<?> reActivateCustomer(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateCustomer(request);
    }


}
