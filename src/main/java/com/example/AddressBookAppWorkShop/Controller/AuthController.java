package com.example.AddressBookAppWorkShop.Controller;

import com.example.AddressBookAppWorkShop.DTO.UserDTO;
import com.example.AddressBookAppWorkShop.service.AuthService;
import com.example.AddressBookAppWorkShop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    //  Explicit constructor to initialize final fields
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.register(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return authService.login(userDTO);
    }


}
