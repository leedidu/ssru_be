package com.example.ssru.account.repository;

import com.example.ssru.account.entity.Account;
import com.example.ssru.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findAllByUser(User user);
}