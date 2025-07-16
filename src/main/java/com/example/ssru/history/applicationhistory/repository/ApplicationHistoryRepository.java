package com.example.ssru.history.applicationhistory.repository;

import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationHistoryRepository extends JpaRepository<ApplicationHistory, Integer> {
    List<ApplicationHistory> findAllByUserAndProgressOrderByCreatedDesc(User user, String progress);
    List<ApplicationHistory> findAllByUserOrderByCreatedDesc(User user);
}