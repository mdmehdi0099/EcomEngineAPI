package com.Ecom.EcomEngineAPI.controller;

import com.Ecom.EcomEngineAPI.dto.AuthResponse;
import com.Ecom.EcomEngineAPI.dto.RegisterRequest;
import com.Ecom.EcomEngineAPI.dto.UserActivationDeactivationRequestDTO;
import com.Ecom.EcomEngineAPI.entity.User;
import com.Ecom.EcomEngineAPI.service.AdminService;
import com.Ecom.EcomEngineAPI.service.AuthService;
import org.springframework.aop.framework.autoproxy.target.LazyInitTargetSourceCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //deactivate Agent User by email
    @PutMapping("/deactivateAgentUser")
    public ResponseEntity<?> deactivateAgentUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateAgentUser(request);
    }

    //reActivate Agent User by email
    @PutMapping("/reActivateAgentUser")
    public ResponseEntity<?> reActivateAgentUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateAgentUser(request);
    }

    //deactivate Seller User by email
    @PutMapping("/deactivateSellerUser")
    public ResponseEntity<?> deactivateSellerUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateSellerUser(request);
    }

    //reActivate Seller User by email
    @PutMapping("/reActivateSellerUser")
    public ResponseEntity<?> reActivateSellerUser(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateSellerUser(request);
    }

    //deactivate Customer by email
    @PutMapping("/deactivateCustomer")
    public ResponseEntity<?> deactivateCustomer(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateCustomer(request);
    }

    //reActivate Customer by email
    @PutMapping("/reActivateCustomer")
    public ResponseEntity<?> reActivateCustomer(@RequestBody UserActivationDeactivationRequestDTO request) {
        return adminService.activateDeActivateCustomer(request);
    }

    //get the list of users on the basis of the roles
    @GetMapping("/users/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role){
        try {
            List<User> users = adminService.getUsersByRole(role);
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found for the role :" + role);
            }
            return ResponseEntity.ok(users);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //get the details of the user
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUsersById(@PathVariable Long userId){
        try {
            User user = adminService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}
