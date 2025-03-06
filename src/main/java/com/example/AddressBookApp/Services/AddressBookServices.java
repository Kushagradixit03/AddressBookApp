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

    public AddressBook addContact(AddressBook addressBook) {
        return addressBookRepository.save(addressBook);
    }

    public void deleteContact(Long id) {
        addressBookRepository.deleteById(id);
    }

    public AddressBook updateContact(Long id, AddressBook addressBook) {
        if (addressBookRepository.existsById(id)) {
            addressBook.setId(id);
            return addressBookRepository.save(addressBook);
        }
        return null;
    }
}
