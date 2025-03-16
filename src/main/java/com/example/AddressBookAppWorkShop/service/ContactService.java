package com.example.AddressBookAppWorkShop.service;

import com.example.AddressBookAppWorkShop.DTO.ResponseDTO;
import com.example.AddressBookAppWorkShop.model.Contact;
import com.example.AddressBookAppWorkShop.repository.ContactRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public ResponseDTO<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return new ResponseDTO<List<Contact>>("All contacts retrieved successfully", contacts);
    }

    public ResponseDTO<Contact> getContactById(Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        return contact.map(c -> new ResponseDTO<Contact>("Contact retrieved successfully", c))
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public ResponseDTO<Contact> addContact(Contact contact) {
        Contact savedContact = contactRepository.save(contact);
        return new ResponseDTO<Contact>("Contact added successfully", savedContact);
    }

    public ResponseDTO<Contact> updateContact(Long id, Contact updatedContact) {
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        existingContact.setName(updatedContact.getName());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setPhone(updatedContact.getPhone());

        Contact savedContact = contactRepository.save(existingContact);
        return new ResponseDTO<Contact>("Contact updated successfully", savedContact);
    }

    public ResponseDTO<String> deleteContact(Long id) {
        contactRepository.deleteById(id);
        return new ResponseDTO<String>("Contact deleted successfully", "Deleted contact ID: " + id);
    }
}
