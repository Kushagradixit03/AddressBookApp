package com.example.AddressBookAppWorkShop.service;

import com.example.AddressBookAppWorkShop.DTO.UserDTO;
import com.example.AddressBookAppWorkShop.model.User;
import com.example.AddressBookAppWorkShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    public String register(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return "Email already exists!";
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);

        // Publish event to RabbitMQ
        rabbitTemplate.convertAndSend("addressbook.exchange", "user.register", user.getEmail());

        return "User registered successfully!";
    }
}
