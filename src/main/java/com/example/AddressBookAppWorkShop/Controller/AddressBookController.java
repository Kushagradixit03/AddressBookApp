package com.example.AddressBookAppWorkShop.Controller;

import com.example.AddressBookAppWorkShop.DTO.AddressBookEntryDTO;
import com.example.AddressBookAppWorkShop.model.AddressBookEntry;
import com.example.AddressBookAppWorkShop.service.AddressBookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address-book")
public class AddressBookController {

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @PostMapping
    public ResponseEntity<AddressBookEntry> addEntry(@Valid @RequestBody AddressBookEntryDTO entryDTO) {
        AddressBookEntry savedEntry = addressBookService.addEntry(entryDTO);
        return ResponseEntity.ok(savedEntry);
    }

    @GetMapping
    public ResponseEntity<List<AddressBookEntryDTO>> getAllEntries() {
        List<AddressBookEntryDTO> entries = addressBookService.getAllEntries();
        return ResponseEntity.ok(entries);
    }
}
