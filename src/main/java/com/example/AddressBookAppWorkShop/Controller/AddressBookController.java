package com.example.AddressBookAppWorkShop.Controller;

import com.example.AddressBookAppWorkShop.DTO.ResponseDTO;
import com.example.AddressBookAppWorkShop.interfaces.IAddressBookService;
import com.example.AddressBookAppWorkShop.model.Contact;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressbook")
public class AddressBookController {

    private final IAddressBookService addressBookService;

    public AddressBookController(IAddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @GetMapping
    public ResponseDTO<List<Contact>> getAllContacts() {
        return addressBookService.getAllContacts();
    }

    @GetMapping("/{id}")
    public ResponseDTO<Contact> getContactById(@PathVariable Long id) {
        return addressBookService.getContactById(id);
    }

    @PostMapping
    public ResponseDTO<Contact> addContact(@RequestBody Contact contact) {
        return addressBookService.addContact(contact);
    }

    @PutMapping("/{id}")
    public ResponseDTO<Contact> updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        return addressBookService.updateContact(id, updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<String> deleteContact(@PathVariable Long id) {
        return addressBookService.deleteContact(id);
    }
}
