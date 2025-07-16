package com.example.ssru.user.repository;

import com.example.ssru.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByLoginId(String id);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    Boolean existsByNickname(String nickname);
    User findByLoginId(String loginId);
}