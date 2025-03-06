package com.example.AddressBookApp.Controller;

import com.example.AddressBookApp.Entity.AddressBook;
import com.example.AddressBookApp.Services.AddressBookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    @Autowired
    private AddressBookServices addressBookService;

    @GetMapping
    public List<AddressBook> getAllContacts() {
        return addressBookService.getAllContacts();
    }

    @GetMapping("/{id}")
    public Optional<AddressBook> getContactById(@PathVariable Long id) {
        return addressBookService.getContactById(id);
    }

    @PostMapping
    public AddressBook addContact(@RequestBody AddressBook addressBook) {
        return addressBookService.addContact(addressBook);
    }

    @PutMapping("/{id}")
    public AddressBook updateContact(@PathVariable Long id, @RequestBody AddressBook addressBook) {
        return addressBookService.updateContact(id, addressBook);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        addressBookService.deleteContact(id);
    }
}
