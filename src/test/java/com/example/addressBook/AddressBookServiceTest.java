package com.example.addressBook;

import com.example.addressBook.dto.ContactDTO;
import com.example.addressBook.model.Contact;
import com.example.addressBook.repository.ContactRepository;
import com.example.addressBook.service.AddressBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressBookServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AddressBookService addressBookService;

    private Contact contact;
    private ContactDTO contactDTO;

    @BeforeEach
    void setUp() {
        contact = new Contact();
        contact.setId(1L);
        contact.setName("John Doe");
        contact.setEmail("john@example.com");
        contact.setPhone("1234567890");

        contactDTO = new ContactDTO();
        contactDTO.setName("John Doe");
        contactDTO.setEmail("john@example.com");
        contactDTO.setPhone("1234567890");
    }

    // ✅ Test Get All Contacts
    @Test
    void testGetAllContacts() {
        when(contactRepository.findAll()).thenReturn(Arrays.asList(contact));
        when(modelMapper.map(any(Contact.class), eq(ContactDTO.class))).thenReturn(contactDTO);

        List<ContactDTO> contacts = addressBookService.getAllContacts();

        assertEquals(1, contacts.size());
        assertEquals("John Doe", contacts.get(0).getName());
        verify(contactRepository, times(1)).findAll();
    }

    // ✅ Test Get Contact By ID (Success)
    @Test
    void testGetContactByIdSuccess() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(modelMapper.map(any(Contact.class), eq(ContactDTO.class))).thenReturn(contactDTO);

        ContactDTO result = addressBookService.getContactById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(contactRepository, times(1)).findById(1L);
    }

    // ✅ Test Get Contact By ID (Throws Exception)
    @Test
    void testGetContactByIdThrowsException() {
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> addressBookService.getContactById(99L));

        assertEquals("Contact not found with ID: 99", exception.getMessage());
    }

    // ✅ Test Save Contact
    @Test
    void testSaveContact() {
        when(modelMapper.map(any(ContactDTO.class), eq(Contact.class))).thenReturn(contact);
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        when(modelMapper.map(any(Contact.class), eq(ContactDTO.class))).thenReturn(contactDTO);

        ContactDTO savedContact = addressBookService.saveContact(contactDTO);

        assertNotNull(savedContact);
        assertEquals("John Doe", savedContact.getName());
        verify(contactRepository, times(1)).save(contact);
    }

    // ✅ Test Update Contact (Success)
    @Test
    void testUpdateContactSuccess() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        when(modelMapper.map(any(Contact.class), eq(ContactDTO.class))).thenReturn(contactDTO);

        ContactDTO updatedContact = addressBookService.updateContact(1L, contactDTO);

        assertNotNull(updatedContact);
        assertEquals("John Doe", updatedContact.getName());
        verify(contactRepository, times(1)).save(contact);
    }

    // ✅ Test Update Contact (Throws Exception)
    @Test
    void testUpdateContactThrowsException() {
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> addressBookService.updateContact(99L, contactDTO));

        assertEquals("Contact not found with ID: 99", exception.getMessage());
    }

    // ✅ Test Delete Contact (Success)

    @Test
    void testDeleteContactSuccess() {
        when(contactRepository.existsById(1L)).thenReturn(true);
        doNothing().when(contactRepository).deleteById(1L);

        boolean result = addressBookService.deleteContact(1L);

        assertTrue(result); // Verify the method returns true on successful deletion
        verify(contactRepository, times(1)).deleteById(1L); // Ensure deleteById is called exactly once
    }


    // ✅ Test Delete Contact (Throws Exception)
    @Test
    void testDeleteContactThrowsException() {
        when(contactRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class,
                () -> addressBookService.deleteContact(99L));

        assertEquals("Contact not found with ID: 99", exception.getMessage());

        verify(contactRepository, never()).deleteById(anyLong()); // Ensure deleteById is never called
    }

}
