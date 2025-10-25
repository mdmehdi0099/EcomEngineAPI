package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.UserActivationDeactivationRequestDTO;
import com.Ecom.EcomEngineAPI.dto.UserActivationDeactivationResponseDTO;
import com.Ecom.EcomEngineAPI.entity.User;
import com.Ecom.EcomEngineAPI.repo.UserRepository;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<UserActivationDeactivationResponseDTO> activateDeActivateAgentUser(UserActivationDeactivationRequestDTO req) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(req.getEmail()));
        if (optionalUser.isEmpty()) {
            UserActivationDeactivationResponseDTO UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                    .email(req.getEmail())
                    .message("User not found")
                    .active(req.isActive())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserActivationResponseDTO);
        }
        UserActivationDeactivationResponseDTO UserActivationResponseDTO = null;
        User user = userRepository.findByEmail(req.getEmail());
        if(user.getRole().equals("AGENT")) {
            if (!req.isActive()) {
                user.setActive(req.isActive());
                user.setDeactivationReason(req.getDeactivationReason());
                user.setDeactivatedAt(LocalDateTime.now());
                userRepository.save(user);
                UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                        .email(req.getEmail())
                        .message("Deactivated successfully")
                        .active(user.isActive())
                        .build();
            } else if (req.isActive()) {
                user.setActive(req.isActive());
                user.setReActivationReason(req.getReActivationReason());
                user.setReActivatedAt(LocalDateTime.now());
                userRepository.save(user);
                UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                        .email(req.getEmail())
                        .message("Reactivated successfully")
                        .active(user.isActive())
                        .build();
            }
        }else{
            UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                    .email(req.getEmail())
                    .message("User is of different roles :"+user.getRole())
                    .active(user.isActive())
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserActivationResponseDTO);
    }

    public ResponseEntity<UserActivationDeactivationResponseDTO> activateDeActivateSellerUser(UserActivationDeactivationRequestDTO req) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(req.getEmail()));
        if (optionalUser.isEmpty()) {
            UserActivationDeactivationResponseDTO UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                    .email(req.getEmail())
                    .message("User not found")
                    .active(req.isActive())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserActivationResponseDTO);
        }
        UserActivationDeactivationResponseDTO UserActivationResponseDTO = null;
        User user = userRepository.findByEmail(req.getEmail());
        if(user.getRole().equals("SELLER")) {
            if (!req.isActive()) {
                user.setActive(req.isActive());
                user.setDeactivationReason(req.getDeactivationReason());
                user.setDeactivatedAt(LocalDateTime.now());
                userRepository.save(user);
                UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                        .email(req.getEmail())
                        .message("Deactivated successfully")
                        .active(user.isActive())
                        .build();

            } else if (req.isActive()) {
                user.setActive(req.isActive());
                user.setReActivationReason(req.getReActivationReason());
                user.setReActivatedAt(LocalDateTime.now());
                userRepository.save(user);
                UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                        .email(req.getEmail())
                        .message("Reactivated successfully")
                        .active(user.isActive())
                        .build();
            }
        }else{
            UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                    .email(req.getEmail())
                    .message("User is of different roles :"+user.getRole())
                    .active(user.isActive())
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserActivationResponseDTO);
    }

    public ResponseEntity<UserActivationDeactivationResponseDTO> activateDeActivateCustomer(UserActivationDeactivationRequestDTO req) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(req.getEmail()));
        if (optionalUser.isEmpty()) {
            UserActivationDeactivationResponseDTO UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                    .email(req.getEmail())
                    .message("User not found")
                    .active(req.isActive())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserActivationResponseDTO);
        }

        UserActivationDeactivationResponseDTO UserActivationResponseDTO = null;
        User user = userRepository.findByEmail(req.getEmail());

        if(user.getRole().equals("CUSTOMER")) {
            if (!req.isActive()) {
                user.setActive(req.isActive());
                user.setDeactivationReason(req.getDeactivationReason());
                user.setDeactivatedAt(LocalDateTime.now());
                userRepository.save(user);
                UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                        .email(req.getEmail())
                        .message("Deactivated successfully")
                        .active(user.isActive())
                        .build();
            } else if (req.isActive()) {
                user.setActive(req.isActive());
                user.setReActivationReason(req.getReActivationReason());
                user.setReActivatedAt(LocalDateTime.now());
                userRepository.save(user);
                UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                        .email(req.getEmail())
                        .message("Reactivated successfully")
                        .active(user.isActive())
                        .build();
            }
        }else{
            UserActivationResponseDTO = UserActivationDeactivationResponseDTO.builder()
                    .email(req.getEmail())
                    .message("User is of different roles :"+user.getRole())
                    .active(user.isActive())
                    .build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(UserActivationResponseDTO);
    }




}
