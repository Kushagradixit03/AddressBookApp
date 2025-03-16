package com.example.AddressBookAppWorkShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AddressBookAppWorkShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(AddressBookAppWorkShopApplication.class, args);
	}
}

