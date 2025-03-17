package com.example.AddressBookAppWorkShop.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    @RabbitListener(queues = "user.registration.queue")
    public void sendRegistrationEmail(String email) {
        System.out.println("Sending registration email to: " + email);
        // Add email sending logic here (SMTP)
    }
}
