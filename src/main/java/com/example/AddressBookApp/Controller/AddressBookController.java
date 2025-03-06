package com.example.AddressBookApp.Controller;

import com.example.AddressBookApp.DTO.AddressBookDTO;
import com.example.AddressBookApp.Model.AddressBook;
import com.example.AddressBookApp.Services.AddressBookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    @Autowired
    private AddressBookServices addressBookService;

    // GET: Get all contacts
    @GetMapping
    public ResponseEntity<List<AddressBookDTO>> getAllContacts() {
        List<AddressBookDTO> contactDTOs = addressBookService.getAllContacts(); // Should return List<AddressBookDTO>
        return new ResponseEntity<>(contactDTOs, HttpStatus.OK); // Return List<AddressBookDTO>
    }

    // GET: Get contact by ID
    @GetMapping("/{id}")
    public ResponseEntity<AddressBookDTO> getContactById(@PathVariable Long id) {
        Optional<AddressBookDTO> contact = addressBookService.getContactById(id); // Should return AddressBookDTO
        return contact.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST: Create a new contact
    @PostMapping
    public ResponseEntity<AddressBookDTO> createContact(@RequestBody AddressBookDTO addressBookDTO) {
        AddressBookDTO createdContact = addressBookService.createContact(addressBookDTO); // Handle AddressBookDTO
        return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
    }

    // PUT: Update a contact by ID
    @PutMapping("/{id}")
    public ResponseEntity<AddressBookDTO> updateContact(@PathVariable Long id, @RequestBody AddressBookDTO addressBookDTO) {
        AddressBookDTO updatedContact = addressBookService.updateContact(id, addressBookDTO); // Handle AddressBookDTO
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
