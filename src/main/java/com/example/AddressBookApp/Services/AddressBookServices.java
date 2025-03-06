package com.example.AddressBookApp.Services;


import com.example.AddressBookApp.Entity.AddressBook;
import com.example.AddressBookApp.Repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBookServices {

    @Autowired
    private AddressBookRepository addressBookRepository;

    public List<AddressBook> getAllContacts() {
        return addressBookRepository.findAll();
    }

    public Optional<AddressBook> getContactById(Long id) {
        return addressBookRepository.findById(id);
    }

    public AddressBook createContact(AddressBook addressBook) {
        return addressBookRepository.save(addressBook);
    }

    public AddressBook updateContact(Long id, AddressBook addressBookDetails) {
        if (addressBookRepository.existsById(id)) {
            addressBookDetails.setId(id);
            return addressBookRepository.save(addressBookDetails);
        }
        return null; // Or throw custom exception
    }

    public boolean deleteContact(Long id) {
        if (addressBookRepository.existsById(id)) {
            addressBookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
