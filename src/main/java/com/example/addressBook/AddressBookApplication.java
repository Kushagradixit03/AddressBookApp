package com.example.addressBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@EnableCaching
public class AddressBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressBookApplication.class, args);
	}

}
