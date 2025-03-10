package com.example.AddressBookApp.Controller;

import com.example.AddressBookApp.Model.AddressBookEntry;
import com.example.AddressBookApp.Services.AddressBookServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j // Enables Lombok Logging
@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    @Autowired
    private AddressBookServices addressBookService;

    // GET all address book entries
    @GetMapping
    public ResponseEntity<List<AddressBookEntry>> getAllEntries() {
        log.info("Received request to fetch all AddressBook entries");
        List<AddressBookEntry> entries = addressBookService.getAllEntries();
        return ResponseEntity.ok(entries);
    }

    // GET address book entry by ID
    @GetMapping("/{id}")
    public ResponseEntity<AddressBookEntry> getEntryById(@PathVariable Long id) {
        log.info("Received request to fetch AddressBook entry with ID: {}", id);
        return addressBookService.getAddressBookEntryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("AddressBook entry with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // POST create a new address book entry
    @PostMapping
    public ResponseEntity<AddressBookEntry> addEntry(@RequestBody AddressBookEntry entry) {
        log.info("Received request to add new AddressBook entry: {}", entry);
        AddressBookEntry createdEntry = addressBookService.addAddressBookEntry(entry);
        return ResponseEntity.ok(createdEntry);
    }

    // PUT update an existing address book entry
    @PutMapping("/{id}")
    public ResponseEntity<AddressBookEntry> updateEntry(@PathVariable Long id, @RequestBody AddressBookEntry updatedEntry) {
        log.info("Received request to update AddressBook entry with ID: {}", id);
        return addressBookService.updateAddressBookEntry(id, updatedEntry)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("AddressBook entry with ID: {} not found for update", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // DELETE an address book entry
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        log.info("Received request to delete AddressBook entry with ID: {}", id);
        boolean deleted = addressBookService.deleteAddressBookEntry(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
