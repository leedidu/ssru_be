package com.example.ssru.address.repository;

import com.example.ssru.address.entity.Address;
import com.example.ssru.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findAddressByUser(User user);
}