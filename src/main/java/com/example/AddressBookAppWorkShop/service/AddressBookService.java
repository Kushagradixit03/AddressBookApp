package com.example.AddressBookAppWorkShop.service;

import com.example.AddressBookAppWorkShop.DTO.ResponseDTO;
import com.example.AddressBookAppWorkShop.interfaces.IAddressBookService;
import com.example.AddressBookAppWorkShop.model.Contact;
import com.example.AddressBookAppWorkShop.repository.ContactRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService implements IAddressBookService {

    private final ContactRepository contactRepository;

    public AddressBookService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public ResponseDTO<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return new ResponseDTO<>("Contacts fetched successfully", contacts);
    }

    @Override
    public ResponseDTO<Contact> getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));
        return new ResponseDTO<>("Contact retrieved successfully", contact);
    }

    @Override
    public ResponseDTO<Contact> addContact(Contact contact) {
        Contact savedContact = contactRepository.save(contact);
        return new ResponseDTO<>("Contact added successfully", savedContact);
    }

    @Override
    public ResponseDTO<Contact> updateContact(Long id, Contact updatedContact) {
        return contactRepository.findById(id)
                .map(existingContact -> {
                    if (updatedContact.getName() != null) {
                        existingContact.setName(updatedContact.getName());
                    }
                    if (updatedContact.getEmail() != null) {
                        existingContact.setEmail(updatedContact.getEmail());
                    }
                    if (updatedContact.getPhone() != null) {
                        existingContact.setPhone(updatedContact.getPhone());
                    }
                    Contact savedContact = contactRepository.save(existingContact);
                    return new ResponseDTO<>("Contact updated successfully", savedContact);
                })
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));
    }

    @Override
    public ResponseDTO<String> deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("Contact not found with ID: " + id);
        }
        contactRepository.deleteById(id);
        return new ResponseDTO<>("Contact deleted successfully", "Deleted contact ID: " + id);
    }
}
