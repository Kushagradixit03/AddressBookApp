package com.example.AddressBookAppWorkShop.service;

import com.example.AddressBookAppWorkShop.model.Contact;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ContactEventListener {

    @RabbitListener(queues = "contact.added.queue")
    public void processNewContact(Contact contact) {
        System.out.println("New contact added: " + contact.getName());
    }
}
