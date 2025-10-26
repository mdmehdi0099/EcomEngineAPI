package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.UserProfileResponseDTO;
import com.Ecom.EcomEngineAPI.entity.User;
import com.Ecom.EcomEngineAPI.repo.UserRepository;
import com.Ecom.EcomEngineAPI.security.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    public ResponseEntity<UserProfileResponseDTO> getMyProfileDetails(HttpServletRequest request){
        UserProfileResponseDTO userProfileResponseDTO=null;
        String header=request.getHeader("Authorization");
        if(header==null || !header.startsWith("Bearer ")){
            userProfileResponseDTO= UserProfileResponseDTO.builder()
                .userId(null)
                .username(null)
                .email(null)
                .phone(null)
                .role(null)
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .isActive(false)
                .deactivatedAt(null)
                .deactivationReason(null)
                .reActivatedAt(null)
                .reActivationReason(null)
                .createdAt(null)
                .updatedAt(null)
                .lastLoginAt(null)
                .message("Authorization token issue, Please check the Authorization")
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userProfileResponseDTO);
        }
        //Extract Token
        String token=header.substring(7);
        //validate the token properly
        try{
            //validate and parse the token
            if (!jwtHelper.validateToken(token)){
                userProfileResponseDTO= UserProfileResponseDTO.builder()
                        .message("Invalid JWT Token")
                        .isActive(false)
                        .isEmailVerified(false)
                        .isPhoneVerified(false)
                        .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userProfileResponseDTO);
            }
        }catch (ExpiredJwtException e){
            userProfileResponseDTO= UserProfileResponseDTO.builder()
                    .message("Token expired. Please log in again.")
                    .isActive(false)
                    .isEmailVerified(false)
                    .isPhoneVerified(false)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userProfileResponseDTO);
        }catch (SignatureException | MalformedJwtException e) {
            userProfileResponseDTO= UserProfileResponseDTO.builder()
                    .message("Invalid token signature or malformed token.")
                    .isActive(false)
                    .isEmailVerified(false)
                    .isPhoneVerified(false)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userProfileResponseDTO);
        }catch (Exception e){
            userProfileResponseDTO= UserProfileResponseDTO.builder()
                    .message("Token validation error :"+e.getMessage())
                    .isActive(false)
                    .isEmailVerified(false)
                    .isPhoneVerified(false)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userProfileResponseDTO);
        }

        String username=jwtHelper.getUsernameFromToken(token);

        //get the user details
        User user=userRepository.findByEmail(username);

        userProfileResponseDTO= UserProfileResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .isEmailVerified(user.isEmailVerified())
                .isPhoneVerified(user.isPhoneVerified())
                .isActive(user.isActive())
                .deactivatedAt(user.getDeactivatedAt())
                .deactivationReason(user.getDeactivationReason())
                .reActivatedAt(user.getReActivatedAt())
                .reActivationReason(user.getReActivationReason())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .message("User details fetched successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(userProfileResponseDTO);
    }

}
