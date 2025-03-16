package com.example.AddressBookAppWorkShop.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDTO<T> {
    private String message;
    private T data;

    // Add this constructor explicitly
    public ResponseDTO(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
