package com.example.ssru.option.repository;

import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.sstealhistory.entity.SstealHistory;
import com.example.ssru.option.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    List<Option> findAllByApplicationHistoryId(int applicationHistoryId);
    List<Option> findAllBySstealHistoryId(int sstealId);
}