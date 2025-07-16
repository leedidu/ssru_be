package com.example.ssru.pointhistory.repository;

import com.example.ssru.pointhistory.entity.PointHistory;
import com.example.ssru.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {
    List<PointHistory> findAllByMerchantUid(String merchantUid);
    List<PointHistory> findAllByUserOrderByCreatedDesc(User user);
    List<PointHistory> findAllByUserAndSituationOrderByCreatedDesc(User user, String situation);
    List<PointHistory> findAllByUserAndCreatedBetweenOrderByCreatedDesc(User user, LocalDateTime startDate, LocalDateTime endDate);
    List<PointHistory> findAllByUserAndSituationAndCreatedBetweenOrderByCreatedDesc(User user, String situation, LocalDateTime startDate, LocalDateTime endDate);

}