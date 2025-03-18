package com.example.AddressBookAppWorkShop.Controller;
import com.example.AddressBookAppWorkShop.DTO.ResponseDTO;
import com.example.AddressBookAppWorkShop.model.Contact;
import com.example.AddressBookAppWorkShop.service.AddressBookService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final AddressBookService contactService;

    @Autowired
    public ContactController(AddressBookService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseDTO<List<Contact>> getAllContacts() {
        return contactService.getAllContacts();
    }

    @PostMapping
    public ResponseDTO<Contact> addContact(@RequestBody Contact contact) {
        return contactService.addContact(contact);
    }
}
