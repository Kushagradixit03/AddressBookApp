package com.example.AddressBookApp.Repository;


import com.example.AddressBookApp.Model.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {
}
