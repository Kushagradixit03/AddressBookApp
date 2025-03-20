package com.example.addressBook.service;

import com.example.addressBook.dto.AuthUserDTO;
import com.example.addressBook.dto.LoginDTO;
import com.example.addressBook.model.AuthUser;
import com.example.addressBook.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AuthenticationService {

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_USER_PREFIX = "USER_";

    public String register(AuthUserDTO userDTO) {
        try {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
            }

            if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
            }

            AuthUser user = new AuthUser();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setHashPass(passwordEncoder.encode(userDTO.getPassword()));

            userRepository.save(user);

            try {
                redisTemplate.opsForValue().set("USER_" + user.getEmail(), user, 10, TimeUnit.MINUTES);
            } catch (Exception e) {
                System.err.println("⚠️ Redis error, skipping caching...");
            }

            return "User registered successfully!";
        } catch (ResponseStatusException e) {
            throw e; // Keep specific errors as is
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed", e);
        }
    }

    public Map<String, String> login(LoginDTO loginDTO) {
        String cacheKey = REDIS_USER_PREFIX + loginDTO.getEmail();
        AuthUser user = null;

        try {
            user = (AuthUser) redisTemplate.opsForValue().get(cacheKey);
            if (user != null) {
                System.out.println("✅ Found user in Redis: " + loginDTO.getEmail());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Redis is down! Falling back to database...");
        }

        if (user == null) {
            try {
                user = userRepository.findByEmail(loginDTO.getEmail())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

                try {
                    redisTemplate.opsForValue().set(cacheKey, user, 10, TimeUnit.MINUTES);
                    System.out.println("✅ User cached in Redis: " + loginDTO.getEmail());
                } catch (Exception e) {
                    System.err.println("⚠️ Redis is still down! Skipping caching...");
                }
            } catch (ResponseStatusException e) {
                throw e;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed", e);
            }
        }

        if (passwordEncoder.matches(loginDTO.getPassword(), user.getHashPass())) {
            String token = jwtTokenService.createToken(user.getId());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            return response;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    public void processForgotPassword(String email) {
        try {
            AuthUser user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

            userRepository.save(user);

            emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to process forgot password", e);
        }
    }

    public void resetPassword(String token, String newPassword) {
        try {
            AuthUser user = userRepository.findByResetToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid or expired token"));

            if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
            }

            user.setHashPass(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password reset failed", e);
        }
    }

    public List<AuthUser> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error retrieving users", e);
        }
    }
}
