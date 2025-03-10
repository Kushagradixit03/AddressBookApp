package com.example.AddressBookApp.Interfaces;

import com.example.AddressBookApp.Model.AddressBookEntry;

import java.util.List;
import java.util.Optional;

public interface IAddressBookServices {

    AddressBookEntry addAddressBookEntry(AddressBookEntry entry);

    List<AddressBookEntry> getAllEntries();

    Optional<AddressBookEntry> getAddressBookEntryById(Long id);

    Optional<AddressBookEntry> updateAddressBookEntry(Long id, AddressBookEntry updatedEntry);

    boolean deleteAddressBookEntry(Long id);
}