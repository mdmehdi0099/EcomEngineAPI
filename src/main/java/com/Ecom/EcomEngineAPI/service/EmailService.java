package com.Ecom.EcomEngineAPI.service;

import com.Ecom.EcomEngineAPI.dto.ForgetPasswordResponseDTO;
import com.Ecom.EcomEngineAPI.dto.ResetPasswordRequestDTO;
import com.Ecom.EcomEngineAPI.dto.ResetPasswordResponseDTO;
import com.Ecom.EcomEngineAPI.entity.User;
import com.Ecom.EcomEngineAPI.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public EmailService(JavaMailSender mailSender,UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.mailSender = mailSender;
        this.userRepository=userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendRegistrationEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration Successful");
        message.setText("Hello " + userName + ",\n\nWelcome! Your registration was successful.\n\nRegards,\nTeam");

        mailSender.send(message);
    }

    @Transactional
    public ForgetPasswordResponseDTO sendForgetPasswordEmail(String toEmail) {
        // ✅ 1. Check if user exists
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(toEmail));
        if (userOptional.isEmpty()) {
            return ForgetPasswordResponseDTO.builder()
                    .Email(toEmail)
                    .Message("User with email " + toEmail + " not found.")
                    .build();
        }

        User user = userOptional.get();

        // ✅ 2. Generate a random 6-digit OTP
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);

        // ✅ 3. Store OTP in the user table
        user.setOtp(code);
        userRepository.save(user);

        // ✅ 4. Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Forgot Password - OTP Verification");
        message.setText("Hello " + user.getRole().name() + ",\n\n"
                + "We received a request to reset your password.\n"
                + "Your OTP for password reset is: " + code + "\n\n"
                + "This OTP will expire in 10 minutes.\n\n"
                + "Regards,\nTeam EcomEngine");

        mailSender.send(message);

        // ✅ 5. Return response
        return ForgetPasswordResponseDTO.builder()
                .Email(toEmail)
                .Message("OTP sent successfully to " + toEmail)
                .build();
    }

    public ResponseEntity<ResetPasswordResponseDTO> resetPassword(ResetPasswordRequestDTO request) {
        // 1️⃣ Find user by email
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(request.getEmail()));
        if (optionalUser.isEmpty()) {
            ResetPasswordResponseDTO resetPasswordResponseDTO=ResetPasswordResponseDTO.builder()
                    .email(request.getEmail())
                    .message("User not found with email: "+request.getEmail())
                    .otp(request.getOtp())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resetPasswordResponseDTO);
        }
        User user = userRepository.findByEmail(request.getEmail());
        // 2️⃣ Validate OTP
        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            ResetPasswordResponseDTO resetPasswordResponseDTO=ResetPasswordResponseDTO.builder()
                    .email(request.getEmail())
                    .message("Please enter the valid OTP")
                    .otp(request.getOtp())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resetPasswordResponseDTO);
        }

        // 3️⃣ Validate password confirmation
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            ResetPasswordResponseDTO resetPasswordResponseDTO=ResetPasswordResponseDTO.builder()
                    .email(request.getEmail())
                    .message("Please enter the valid Password")
                    .otp(request.getOtp())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resetPasswordResponseDTO);
        }

        // 4️⃣ Encrypt and set new password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 5️⃣ Expire OTP
        user.setOtp(null);

        // 6️⃣ (Optional) Invalidate old tokens
        // If you store tokens in DB, mark them expired here
        //jwtHelper.invalidateTokenForUser(user.getEmail());

        // 7️⃣ Save user
        userRepository.save(user);

        // 8️⃣ Return response
        ResetPasswordResponseDTO resetPasswordResponseDTO=ResetPasswordResponseDTO.builder()
                .email(user.getEmail())
                .message("Password reset successful. Please log in again.")
                .otp(request.getOtp())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(resetPasswordResponseDTO);
    }



}
