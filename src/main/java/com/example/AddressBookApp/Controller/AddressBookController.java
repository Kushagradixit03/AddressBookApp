package com.example.AddressBookApp.Controller;

import com.example.AddressBookApp.Entity.AddressBook;
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

    @Autowired
    private AddressBookServices addressBookService;

    // GET: Get all contacts
    @GetMapping
    public ResponseEntity<List<AddressBook>> getAllContacts() {
        List<AddressBook> contacts = addressBookService.getAllContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    // GET: Get contact by ID
    @GetMapping("/{id}")
    public ResponseEntity<AddressBook> getContactById(@PathVariable Long id) {
        Optional<AddressBook> contact = addressBookService.getContactById(id);
        return contact.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST: Create a new contact
    @PostMapping
    public ResponseEntity<AddressBook> createContact(@RequestBody AddressBook addressBook) {
        AddressBook createdContact = addressBookService.createContact(addressBook);
        return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
    }

    // PUT: Update a contact by ID
    @PutMapping("/{id}")
    public ResponseEntity<AddressBook> updateContact(@PathVariable Long id, @RequestBody AddressBook addressBookDetails) {
        AddressBook updatedContact = addressBookService.updateContact(id, addressBookDetails);
        return updatedContact != null ? new ResponseEntity<>(updatedContact, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE: Delete a contact by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        boolean isDeleted = addressBookService.deleteContact(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
