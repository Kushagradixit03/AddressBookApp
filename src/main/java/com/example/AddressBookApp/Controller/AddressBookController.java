package com.example.AddressBookApp.Controller;

import com.example.AddressBookApp.Model.AddressBook;
import com.example.AddressBookApp.Services.AddressBookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    private final AddressBookServices addressBookService;

    // Constructor-based dependency injection (recommended)
    @Autowired
    public AddressBookController(AddressBookServices addressBookService) {
        this.addressBookService = addressBookService;
    }

    // Endpoint to create a new contact
    @PostMapping
    public ResponseEntity<AddressBook> createContact(@RequestBody AddressBook addressBook) {
        AddressBook newContact = addressBookService.createContact(addressBook);
        return new ResponseEntity<>(newContact, HttpStatus.CREATED);
    }

    // Endpoint to get all contacts
    @GetMapping
    public ResponseEntity<List<AddressBook>> getAllContacts() {
        List<AddressBook> contacts = addressBookService.getAllContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    // Endpoint to get a contact by id
    @GetMapping("/{id}")
    public ResponseEntity<AddressBook> getContactById(@PathVariable Long id) {
        Optional<AddressBook> addressBook = addressBookService.getContactById(id);
        if (addressBook.isPresent()) {
            return new ResponseEntity<>(addressBook.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint to update a contact
    @PutMapping("/{id}")
    public ResponseEntity<AddressBook> updateContact(@PathVariable Long id, @RequestBody AddressBook addressBook) {
        AddressBook updatedContact = addressBookService.updateContact(id, addressBook);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    // Endpoint to delete a contact
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        addressBookService.deleteContact(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
