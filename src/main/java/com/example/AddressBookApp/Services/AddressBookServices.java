package com.example.AddressBookApp.Services;

import com.example.AddressBookApp.Model.AddressBookEntry;
import com.example.AddressBookApp.Interfaces.IAddressBookServices;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressBookServices implements IAddressBookServices {

    private List<AddressBookEntry> addressBookList = new ArrayList<>();

    // Create or Add AddressBook Entry
    @Override
    public AddressBookEntry addAddressBookEntry(AddressBookEntry entry) {
        addressBookList.add(entry);
        return entry;
    }

    // Get all AddressBook Entries
    @Override
    public List<AddressBookEntry> getAllEntries() {
        return addressBookList;
    }

    // Get AddressBook Entry by ID
    @Override
    public Optional<AddressBookEntry> getAddressBookEntryById(Long id) {
        return addressBookList.stream().filter(entry -> entry.getId().equals(id)).findFirst();
    }

    // Update AddressBook Entry by ID
    @Override
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
    @Override
    public boolean deleteAddressBookEntry(Long id) {
        return addressBookList.removeIf(entry -> entry.getId().equals(id));
    }
}
