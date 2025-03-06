package com.example.AddressBookApp.Services;

import com.example.AddressBookApp.Model.AddressBook;
import com.example.AddressBookApp.Repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBookServices {

    private final AddressBookRepository addressBookRepository;

    // Constructor-based dependency injection (best practice)
    @Autowired
    public AddressBookServices(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    // Create a new address book entry
    public AddressBook createContact(AddressBook addressBook) {
        return addressBookRepository.save(addressBook);
    }

    // Get all address book entries
    public List<AddressBook> getAllContacts() {
        return addressBookRepository.findAll();
    }

    // Get a specific contact by id
    public Optional<AddressBook> getContactById(Long id) {
        return addressBookRepository.findById(id);
    }

    // Update an existing contact by id
    public AddressBook updateContact(Long id, AddressBook addressBookDetails) {
        AddressBook addressBook = addressBookRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
        addressBook.setName(addressBookDetails.getName());
        addressBook.setEmail(addressBookDetails.getEmail());
        addressBook.setPhoneNumber(addressBookDetails.getPhoneNumber());
        return addressBookRepository.save(addressBook);
    }

    // Delete a contact by id
    public void deleteContact(Long id) {
        AddressBook addressBook = addressBookRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
        addressBookRepository.delete(addressBook);
    }
}
