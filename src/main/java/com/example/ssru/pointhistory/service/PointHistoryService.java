package com.example.ssru.pointhistory.service;

import com.example.ssru.exception.CustomException;
import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.pointhistory.dto.PointHistoryResponseDto;
import com.example.ssru.pointhistory.entity.PointHistory;
import com.example.ssru.pointhistory.mapper.PointHistoryMapper;
import com.example.ssru.pointhistory.repository.PointHistoryRepository;
import com.example.ssru.user.entity.User;
import com.example.ssru.user.repository.UserRepository;
import com.example.ssru.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PointHistoryService {

    private PointHistoryRepository pointHistoryRepository;
    private UserService userService;
    private UserRepository userRepository;
    private PointHistoryMapper pointHistoryMapper;

    public PointHistory createPointHistory(PointHistory pointHistory){
        System.out.println("----- 포인트 내역 등록 -----");
        User user = userService.getLoginUser();
        pointHistory.setUser(user);
        if(pointHistory.getSituation().equals("use")){
            System.out.println("사용 내역 ---");
            user.setPoint(user.getPoint() - pointHistory.getAmount());
        } else if(pointHistory.getSituation().equals("charge")){
            System.out.println("충전 내역 ---");
            boolean check = pointHistoryRepository.findAllByMerchantUid(pointHistory.getMerchantUid()).isEmpty();
            System.out.println("주문번호를 통해 중복확인 : " + check);
            if(!check) return null;
            user.setPoint(user.getPoint() + pointHistory.getAmount());
        } else{
            System.out.println("적립 내역 ---");
            user.setPoint(user.getPoint() + pointHistory.getAmount());
        }
        userRepository.save(user);
        System.out.println("----- 포인트 내역 성공 -----");
        return pointHistoryRepository.save(pointHistory);
    }

    public List<PointHistoryResponseDto.PointHistoryListResponse> getPointHistoryList(String situation){
        User user = userService.getLoginUser();
        List<PointHistory> pointHistories = new ArrayList<>();
        if(situation.equals("all")){
            pointHistories = pointHistoryRepository.findAllByUserOrderByCreatedDesc(user);
        } else{
            pointHistories = pointHistoryRepository.findAllByUserAndSituationOrderByCreatedDesc(user, situation);
        }
        return pointHistories.stream().map(pointHistory -> pointHistoryMapper.toPointHistoryListResponseDto(pointHistory)).collect(Collectors.toList());
    }

    public List<PointHistoryResponseDto.PointHistoryListResponse> getPointHistoryListByDate(String situation, String start, String end){
        User user = userService.getLoginUser();
        LocalDateTime startDate = LocalDateTime.parse(start + "T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse(end + "T00:00:00").plusDays(1);
        List<PointHistory> pointHistories = new ArrayList<>();
        if(situation.equals("all")){
            pointHistories = pointHistoryRepository.findAllByUserAndCreatedBetweenOrderByCreatedDesc(user, startDate, endDate);
        } else{
            pointHistories = pointHistoryRepository.findAllByUserAndSituationAndCreatedBetweenOrderByCreatedDesc(user, situation, startDate, endDate);
        }
        return pointHistories.stream().map(pointHistory -> pointHistoryMapper.toPointHistoryListResponseDto(pointHistory)).collect(Collectors.toList());
    }

    public PointHistory findPointHistory(int pointHistoryId){
        return verifiedPointHistory(pointHistoryId);
    }

    public PointHistory patchPointHistory(PointHistory pointHistory){
        PointHistory pointHistory1 = findPointHistory(pointHistory.getId());
        return pointHistoryRepository.save(pointHistory1);
    }

    public PointHistory verifiedPointHistory(int pointHistoryId){
        Optional<PointHistory> user = pointHistoryRepository.findById(pointHistoryId);
        return user.orElseThrow(() -> new CustomException(CustomExceptionCode.POINT_HISTORY_NOT_FOUND));
    }

    public void deletePointHistory(int pointHistoryId){
        PointHistory pointHistory1 = verifiedPointHistory(pointHistoryId);
        pointHistoryRepository.delete(pointHistory1);
    }
}
