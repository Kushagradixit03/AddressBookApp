package com.example.AddressBookApp.Controller;
import com.example.AddressBookApp.Model.AddressBookEntry;
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

    // GET all address book entries
    @GetMapping
    public List<AddressBookEntry> getAllEntries() {
        return addressBookService.getAllEntries();
    }

    // GET address book entry by ID
    @GetMapping("/{id}")
    public Optional<AddressBookEntry> getAddressBookEntryById(@PathVariable Long id) {
        return addressBookService.getAddressBookEntryById(id);
    }

    // POST create a new address book entry
    @PostMapping
    public AddressBookEntry addAddressBookEntry(@RequestBody AddressBookEntry entry) {
        return addressBookService.addAddressBookEntry(entry);
    }

    // PUT update an existing address book entry
    @PutMapping("/{id}")
    public Optional<AddressBookEntry> updateAddressBookEntry(@PathVariable Long id, @RequestBody AddressBookEntry updatedEntry) {
        return addressBookService.updateAddressBookEntry(id, updatedEntry);
    }

    // DELETE an address book entry
    @DeleteMapping("/{id}")
    public boolean deleteAddressBookEntry(@PathVariable Long id) {
        return addressBookService.deleteAddressBookEntry(id);
    }
}
