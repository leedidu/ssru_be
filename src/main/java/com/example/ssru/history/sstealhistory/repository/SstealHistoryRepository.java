package com.example.ssru.history.sstealhistory.repository;

import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.sstealhistory.entity.SstealHistory;
import com.example.ssru.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SstealHistoryRepository extends JpaRepository<SstealHistory, Integer> {
    List<SstealHistory> findAllBySstealerAndProgressOrderByCreatedDesc(User user, String progress);
    List<SstealHistory> findAllBySstealerOrderByCreatedDesc(User user);
}