package com.example.AddressBookApp.Services;

import com.example.AddressBookApp.Model.AddressBookEntry;
import com.example.AddressBookApp.Interfaces.IAddressBookServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j // Enables Lombok Logging
@Service
public class AddressBookServices implements IAddressBookServices {

    private final List<AddressBookEntry> addressBookList = new ArrayList<>();

    // Create or Add AddressBook Entry
    @Override
    public AddressBookEntry addAddressBookEntry(AddressBookEntry entry) {
        log.info("Adding new AddressBook entry: {}", entry);
        addressBookList.add(entry);
        return entry;
    }

    // Get all AddressBook Entries
    @Override
    public List<AddressBookEntry> getAllEntries() {
        log.info("Fetching all AddressBook entries");
        return addressBookList;
    }

    // Get AddressBook Entry by ID
    @Override
    public Optional<AddressBookEntry> getAddressBookEntryById(Long id) {
        log.info("Fetching AddressBook entry with ID: {}", id);
        return addressBookList.stream()
                .filter(entry -> entry.getId().equals(id))
                .findFirst();
    }

    // Update AddressBook Entry by ID
    @Override
    public Optional<AddressBookEntry> updateAddressBookEntry(Long id, AddressBookEntry updatedEntry) {
        log.info("Updating AddressBook entry with ID: {}", id);
        Optional<AddressBookEntry> entryOptional = getAddressBookEntryById(id);

        if (entryOptional.isPresent()) {
            AddressBookEntry entry = entryOptional.get();
            entry.setName(updatedEntry.getName());
            entry.setPhoneNumber(updatedEntry.getPhoneNumber());
            entry.setEmail(updatedEntry.getEmail());
            log.info("Updated AddressBook entry: {}", entry);
            return Optional.of(entry);
        }

        log.warn("AddressBook entry with ID: {} not found for update", id);
        return Optional.empty();
    }

    // Delete AddressBook Entry by ID
    @Override
    public boolean deleteAddressBookEntry(Long id) {
        log.info("Deleting AddressBook entry with ID: {}", id);
        boolean deleted = addressBookList.removeIf(entry -> entry.getId().equals(id));

        if (deleted) {
            log.info("Successfully deleted AddressBook entry with ID: {}", id);
        } else {
            log.warn("Failed to delete AddressBook entry with ID: {} (not found)", id);
        }

        return deleted;
    }
}
