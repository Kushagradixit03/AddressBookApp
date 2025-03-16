package com.example.AddressBookAppWorkShop.Controller;

import com.example.AddressBookAppWorkShop.DTO.ResponseDTO;
import com.example.AddressBookAppWorkShop.model.Contact;
import com.example.AddressBookAppWorkShop.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseDTO<List<Contact>> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/{id}")
    public ResponseDTO<Contact> getContactById(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    @PostMapping
    public ResponseDTO<Contact> addContact(@RequestBody Contact contact) {
        return contactService.addContact(contact);
    }

    @PutMapping("/{id}")
    public ResponseDTO<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contactDetails) {
        return contactService.updateContact(id, contactDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<String> deleteContact(@PathVariable Long id) {
        return contactService.deleteContact(id);
    }
}
