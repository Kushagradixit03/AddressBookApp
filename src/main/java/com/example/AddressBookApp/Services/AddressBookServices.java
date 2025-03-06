package com.example.AddressBookApp.Services;

import com.example.AddressBookApp.DTO.AddressBookDTO;
import com.example.AddressBookApp.Model.AddressBook;
import com.example.AddressBookApp.Repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressBookServices {

    @Autowired
    private AddressBookRepository addressBookRepository;

    // Get all contacts and return them as DTOs
    public List<AddressBookDTO> getAllContacts() {
        return addressBookRepository.findAll().stream()
                .map(this::convertToDTO)  // Map AddressBook to AddressBookDTO
                .collect(Collectors.toList());
    }

    // Get a contact by ID and return it as a DTO
    public Optional<AddressBookDTO> getContactById(Long id) {
        return addressBookRepository.findById(id).map(this::convertToDTO);
    }

    // Create a new contact and return it as DTO
    public AddressBookDTO createContact(AddressBookDTO addressBookDTO) {
        AddressBook addressBook = convertToEntity(addressBookDTO); // Convert DTO to entity
        AddressBook savedContact = addressBookRepository.save(addressBook);
        return convertToDTO(savedContact); // Convert saved entity back to DTO
    }

    // Update a contact by ID and return it as DTO
    public AddressBookDTO updateContact(Long id, AddressBookDTO addressBookDTO) {
        if (addressBookRepository.existsById(id)) {
            AddressBook addressBook = convertToEntity(addressBookDTO); // Convert DTO to entity
            addressBook.setId(id);
            AddressBook updatedContact = addressBookRepository.save(addressBook);
            return convertToDTO(updatedContact); // Convert updated entity back to DTO
        }
        return null;
    }

    // Delete a contact by ID
    public boolean deleteContact(Long id) {
        if (addressBookRepository.existsById(id)) {
            addressBookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Convert AddressBook entity to AddressBookDTO
    private AddressBookDTO convertToDTO(AddressBook addressBook) {
        AddressBookDTO dto = new AddressBookDTO();
        dto.setId(addressBook.getId());
        dto.setName(addressBook.getName());
        dto.setEmail(addressBook.getEmail());
        dto.setPhoneNumber(addressBook.getPhoneNumber());
        return dto;
    }

    // Convert AddressBookDTO to AddressBook entity
    private AddressBook convertToEntity(AddressBookDTO addressBookDTO) {
        AddressBook addressBook = new AddressBook();
        addressBook.setName(addressBookDTO.getName());
        addressBook.setEmail(addressBookDTO.getEmail());
        addressBook.setPhoneNumber(addressBookDTO.getPhoneNumber());
        return addressBook;
    }
}
