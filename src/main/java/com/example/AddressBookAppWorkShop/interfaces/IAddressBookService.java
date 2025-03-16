package com.example.AddressBookAppWorkShop.interfaces;

import com.example.AddressBookAppWorkShop.DTO.ResponseDTO;
import com.example.AddressBookAppWorkShop.model.Contact;
import java.util.List;

public interface IAddressBookService {
    ResponseDTO<List<Contact>> getAllContacts();
    ResponseDTO<Contact> getContactById(Long id);
    ResponseDTO<Contact> addContact(Contact contact);
    ResponseDTO<Contact> updateContact(Long id, Contact updatedContact);
    ResponseDTO<String> deleteContact(Long id);
}
