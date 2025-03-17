package com.example.AddressBookAppWorkShop.service;

import com.example.AddressBookAppWorkShop.DTO.ResponseDTO;
import com.example.AddressBookAppWorkShop.interfaces.IAddressBookService;
import com.example.AddressBookAppWorkShop.model.Contact;
import com.example.AddressBookAppWorkShop.repository.ContactRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookService implements IAddressBookService {

    private final ContactRepository contactRepository;
    private final RabbitTemplate rabbitTemplate;

    public AddressBookService(ContactRepository contactRepository,RabbitTemplate rabbitTemplate) {
        this.contactRepository = contactRepository;
        this.rabbitTemplate=rabbitTemplate;
    }

    @Override
    @Cacheable(value = "contacts")
    public ResponseDTO<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return new ResponseDTO<>("Contacts fetched successfully", contacts);
    }

    @Override
    @Cacheable(value = "contact", key = "#id")
    public ResponseDTO<Contact> getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));
        return new ResponseDTO<>("Contact retrieved successfully", contact);
    }

    @Override
    @CacheEvict(value = "contacts", allEntries = true)
    public ResponseDTO<Contact> addContact(Contact contact) {
        Contact savedContact = contactRepository.save(contact);

        // Publish event to RabbitMQ
        rabbitTemplate.convertAndSend("addressbook.exchange", "contact.add", savedContact);

        return new ResponseDTO<>("Contact added successfully", savedContact);
    }


    @Override
    @CachePut(value = "contact", key = "#id")
    @CacheEvict(value = "contacts", allEntries = true)
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
    @CacheEvict(value = {"contacts", "contact"}, allEntries = true)
    public ResponseDTO<String> deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("Contact not found with ID: " + id);
        }
        contactRepository.deleteById(id);
        return new ResponseDTO<>("Contact deleted successfully", "Deleted contact ID: " + id);
    }
}
