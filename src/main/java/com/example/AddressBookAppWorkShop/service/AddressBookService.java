
package com.example.AddressBookAppWorkShop.service;


import com.example.AddressBookAppWorkShop.model.AddressBookEntry;
import com.example.AddressBookAppWorkShop.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService {

    @Autowired
    private AddressBookRepository addressBookRepository;

    public List<AddressBookEntry> getAllContacts() {
        return addressBookRepository.findAll();
    }

    public Optional<AddressBookEntry> getContactById(Long id) {
        return addressBookRepository.findById(id);
    }

    public AddressBookEntry addContact(AddressBookEntry entry) {
        return addressBookRepository.save(entry);
    }

    public void deleteContact(Long id) {
        addressBookRepository.deleteById(id);
    }
}
