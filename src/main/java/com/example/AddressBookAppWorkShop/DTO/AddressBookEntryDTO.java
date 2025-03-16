package com.example.AddressBookAppWorkShop.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressBookEntryDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotBlank(message = "Address cannot be empty")
    private String address;
}
