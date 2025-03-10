package com.example.AddressBookApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
//<==========================UC6-Lombok Library ==============================>
public class AddressBookDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
}
