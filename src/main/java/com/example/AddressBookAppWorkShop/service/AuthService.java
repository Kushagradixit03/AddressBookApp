package com.example.AddressBookAppWorkShop.service;

import com.example.AddressBookAppWorkShop.DTO.UserDTO;
import com.example.AddressBookAppWorkShop.model.User;
import com.example.AddressBookAppWorkShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
}
