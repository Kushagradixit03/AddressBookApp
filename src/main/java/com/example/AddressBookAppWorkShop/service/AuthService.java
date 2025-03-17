package com.example.AddressBookAppWorkShop.service;

import com.example.AddressBookAppWorkShop.DTO.ForgotPasswordRequest;
import com.example.AddressBookAppWorkShop.DTO.ResetPasswordRequest;
import com.example.AddressBookAppWorkShop.DTO.UserDTO;
import com.example.AddressBookAppWorkShop.model.User;
import com.example.AddressBookAppWorkShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    // ✅ Login
    public ResponseEntity<?> login(UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                String token = jwtService.generateToken(user.getEmail());

                return ResponseEntity.ok(Map.of(
                        "message", "Login successful",
                        "token", token
                ));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "error", "Invalid email or password"
        ));
    }

    // ✅ Forgot Password: Generate Reset Token & Send Email
    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString(); // Generate token
            user.setResetToken(token);
            userRepository.save(user);

            emailService.sendResetPasswordEmail(user.getEmail(), token); // Send email
            return ResponseEntity.ok("Reset password email sent.");
        }
        return ResponseEntity.badRequest().body("Email not found.");
    }

    // ✅ Reset Password: Verify Token & Update Password
    public ResponseEntity<?> resetPassword(ResetPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByResetToken(request.getToken());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setResetToken(null); // Remove token after reset
            userRepository.save(user);
            return ResponseEntity.ok("Password reset successful.");
        }
        return ResponseEntity.badRequest().body("Invalid or expired token.");
    }
}
