package com.example.AddressBookAppWorkShop.repository;

import com.example.AddressBookAppWorkShop.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
