package com.example.AddressBookAppWorkShop.service;

import com.example.AddressBookAppWorkShop.DTO.AddressBookEntryDTO;
import com.example.AddressBookAppWorkShop.model.AddressBookEntry;
import com.example.AddressBookAppWorkShop.repository.AddressBookRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;
    private final ModelMapper modelMapper;

    public AddressBookService(AddressBookRepository addressBookRepository, ModelMapper modelMapper) {
        this.addressBookRepository = addressBookRepository;
        this.modelMapper = modelMapper;
    }

    public AddressBookEntry addEntry(AddressBookEntryDTO entryDTO) {
        AddressBookEntry entry = modelMapper.map(entryDTO, AddressBookEntry.class);
        return addressBookRepository.save(entry);
    }

    public List<AddressBookEntryDTO> getAllEntries() {
        return addressBookRepository.findAll()
                .stream()
                .map(entry -> modelMapper.map(entry, AddressBookEntryDTO.class))
                .collect(Collectors.toList());
    }
}
