package com.example.addressBook.service;

import com.example.addressBook.dto.ContactDTO;
import com.example.addressBook.model.Contact;
import com.example.addressBook.repository.ContactRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressBookService implements IAddressBookService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ModelMapper modelMapper;

    // ✅ Get all contacts
    @Override
    public List<ContactDTO> getAllContacts() {
        try {
            return contactRepository.findAll().stream()
                    .map(contact -> modelMapper.map(contact, ContactDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving contacts", e);
        }
    }

    // ✅ Get contact by ID
    @Override
    public ContactDTO getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));
        return modelMapper.map(contact, ContactDTO.class);
    }


    // ✅ Save a new contact
    @Override
    public ContactDTO saveContact(ContactDTO contactDTO) {
        try {
            Contact contact = modelMapper.map(contactDTO, Contact.class);
            Contact savedContact = contactRepository.save(contact);
            return modelMapper.map(savedContact, ContactDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error saving contact", e);
        }
    }

    // ✅ Update an existing contact
    @Override
    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));

        existingContact.setName(contactDTO.getName());
        existingContact.setEmail(contactDTO.getEmail());
        existingContact.setPhone(contactDTO.getPhone());

        Contact updatedContact = contactRepository.save(existingContact);
        return modelMapper.map(updatedContact, ContactDTO.class);
    }


    // ✅ Delete contact
    @Override
    public boolean deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("Contact not found with ID: " + id);
        }

        try {
            contactRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting contact", e);
        }
    }

}
