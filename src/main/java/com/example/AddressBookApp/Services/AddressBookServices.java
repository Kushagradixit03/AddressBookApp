package com.example.AddressBookApp.Services;

import com.example.AddressBookApp.Model.AddressBookEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressBookServices {

    private List<AddressBookEntry> addressBookList = new ArrayList<>();

    // Create or Add AddressBook Entry
    public AddressBookEntry addAddressBookEntry(AddressBookEntry entry) {
        addressBookList.add(entry);
        return entry;
    }

    // Get all AddressBook Entries
    public List<AddressBookEntry> getAllEntries() {
        return addressBookList;
    }

    // Get AddressBook Entry by ID
    public Optional<AddressBookEntry> getAddressBookEntryById(Long id) {
        return addressBookList.stream().filter(entry -> entry.getId().equals(id)).findFirst();
    }

    // Update AddressBook Entry by ID
    public Optional<AddressBookEntry> updateAddressBookEntry(Long id, AddressBookEntry updatedEntry) {
        Optional<AddressBookEntry> entryOptional = getAddressBookEntryById(id);
        if (entryOptional.isPresent()) {
            AddressBookEntry entry = entryOptional.get();
            entry.setName(updatedEntry.getName());
            entry.setPhoneNumber(updatedEntry.getPhoneNumber());
            entry.setEmail(updatedEntry.getEmail());
            return Optional.of(entry);
        }
        return Optional.empty();
    }

    // Delete AddressBook Entry by ID
    public boolean deleteAddressBookEntry(Long id) {
        return addressBookList.removeIf(entry -> entry.getId().equals(id));
    }
}
