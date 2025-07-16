package com.example.ssru.history.applicationhistory.service;

import com.example.ssru.history.applicationhistory.dto.ApplicationHistoryRequestDto;
import com.example.ssru.history.applicationhistory.dto.ApplicationHistoryResponseDto;
import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.applicationhistory.mapper.ApplicationHistoryMapper;
import com.example.ssru.history.applicationhistory.repository.ApplicationHistoryRepository;
import com.example.ssru.exception.CustomException;
import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.history.sstealhistory.dto.SstealHistoryResponseDto;
import com.example.ssru.history.sstealhistory.entity.SstealHistory;
import com.example.ssru.option.dto.OptionResponseDto;
import com.example.ssru.option.entity.Option;
import com.example.ssru.option.mapper.OptionMapper;
import com.example.ssru.option.repository.OptionRepository;
import com.example.ssru.option.service.OptionService;
import com.example.ssru.pointhistory.entity.PointHistory;
import com.example.ssru.pointhistory.repository.PointHistoryRepository;
import com.example.ssru.pointhistory.service.PointHistoryService;
import com.example.ssru.s3.S3UploadService;
import com.example.ssru.user.service.UserService;
import com.example.ssru.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApplicationHistoryService {

    private ApplicationHistoryRepository applicationHistoryRepository;
    private UserService userService;
    private OptionRepository optionRepository;
    private ApplicationHistoryMapper applicationHistoryMapper;
    private OptionMapper optionMapper;
    private PointHistoryService pointHistoryService;
    private PointHistoryRepository pointHistoryRepository;
    private S3UploadService s3UploadService;

    public ApplicationHistory createApplicationHistory(ApplicationHistory applicationHistory, List<Option> options){
        System.out.println("----- 수거 신청 시작 -----");
        User user = userService.getLoginUser();
        int userPoint = user.getPoint();
        System.out.println("현재 유저 보유 포인트 : " + userPoint);

        int totalPrice = options.stream()
            .mapToInt(option -> option.getPrice() * option.getCount()) // 가격과 개수를 곱한 값을 int로 매핑
            .sum(); // 모든 값을 합산
        System.out.println("가격 총 합 : " + totalPrice);

        if(userPoint >= totalPrice){
            System.out.println("----- '보유포인트 > 총가격' 이므로 수거 신청 시작 -----");
            System.out.println("----- 개별 옵션 - 신청 내역 매핑 -----");
            for(Option option : options) {
                System.out.println("옵션 아이디 : " + option.getId());
                option.setApplicationHistory(applicationHistory); // Option에 ApplicationHistory 설정
            }
            applicationHistory.setOptionList(options); // ApplicationHistory에 OptionList 설정
            applicationHistory.setPrice(totalPrice);
            applicationHistory.setUser(user);
            System.out.println("----- 신청 내역 -> 옵션리스트/가격/신청 유저 설정 완료 -----");

            PointHistory pointHistory = new PointHistory();
            pointHistory.setSituation("use");
            pointHistory.setAmount(applicationHistory.getPrice());

            ApplicationHistory applicationHistory1 = applicationHistoryRepository.save(applicationHistory);
            pointHistoryService.createPointHistory(pointHistory);
            pointHistory.setApplicationHistory(applicationHistory);
            pointHistoryRepository.save(pointHistory);
            System.out.println("포인트 --> 차감 및 내역 추가 완료");
            System.out.println("포인트 내역 아이디 : " + pointHistory.getId());

            System.out.println("----- 수거 신청 완료 -----");
            return applicationHistory1;
        }
        System.out.println("----- '보유포인트 < 총가격' 이므로 수거 신청 실패 -----");
        return null;
    }

    public void uploadPhoto(int id, MultipartFile photo) throws IOException {
        System.out.println("----- 사진 업로드 시작 -----");
        ApplicationHistory applicationHistory = findApplicationHistory(id);

        String savePhoto = s3UploadService.saveFile(photo);
        applicationHistory.setPhoto(savePhoto);
        applicationHistoryRepository.save(applicationHistory);
        System.out.println("사진 주소 : " + savePhoto);
    }

    public boolean cancelApplication(int applicationId){
        System.out.println("----- 수거 신청 취소 시작 -----");
        User user = userService.getLoginUser();
        if(findApplicationHistory(applicationId) != null){
            ApplicationHistory applicationHistory = findApplicationHistory(applicationId);
            pointHistoryService.deletePointHistory(applicationHistory.getPointHistory().getId());

            List<Option> options = applicationHistory.getOptionList();
            user.setPoint(user.getPoint() + applicationHistory.getPrice());
            System.out.println("포인트 --> 이용 내역 삭제 및 포인트 반환");

            optionRepository.deleteAll(options);
            applicationHistoryRepository.delete(applicationHistory);
            System.out.println("----- 수거 신청 취소 완료 -----");
            return true;
        }
        System.out.println("----- 수거 내역을 찾지 못함 -----");
        return false;
    }

    public List<ApplicationHistoryResponseDto.ApplicationHistoryDetail> getApplicationHistoryList(String progress) {
        System.out.println("----- 수거 신청 내역 리스트 반환 시작 -----");
        User user = userService.getLoginUser();
        Map<String, String> progressMap = new HashMap<String, String>() {{
            put("all", "전체");
            put("in-progress", "신청중"); // 전체, 신청중, 대기중 ,쓰틸완료, 취소
            put("waiting", "대기중");
            put("completed", "완료");
            put("cancelled", "취소");
        }};

        List<ApplicationHistory> applicationHistories = new ArrayList<>();
        String mappedProgress = progressMap.get(progress.trim());
        if (mappedProgress.equals("전체")) {
            System.out.println("----- 전체(all)일 경우 -----");
            applicationHistories = applicationHistoryRepository.findAllByUserOrderByCreatedDesc(user);
        } else{
            System.out.println("----- 전체(all)이 아닌 나머지 경우 -----");
            applicationHistories = applicationHistoryRepository.findAllByUserAndProgressOrderByCreatedDesc(user, mappedProgress);
        }
        System.out.println("총 개수 : " + applicationHistories.size());
        System.out.println("----- 수거 신청 내역 리스트 반환 완료 -----");
        return mapToHistoryDetails(applicationHistories);
    }

    public List<ApplicationHistoryResponseDto.ApplicationHistoryDetail> mapToHistoryDetails(List<ApplicationHistory> applicationHistories) {
        List<ApplicationHistoryResponseDto.ApplicationHistoryDetail> applicationHistoryDetails = new ArrayList<>();

        for (ApplicationHistory applicationHistory : applicationHistories) {
            applicationHistoryDetails.add(applicationHistoryMapper.toApplicationHistoryDetail(applicationHistory));
        }
        return applicationHistoryDetails;
    }

    public ApplicationHistoryResponseDto.ApplicationHistoryDetailById getApplicationDetailInfoById(int applicationId){
        ApplicationHistory applicationHistory = findApplicationHistory(applicationId);
        ApplicationHistoryResponseDto.ApplicationHistoryDetailById applicationHistoryDetailById = applicationHistoryMapper.toApplicationHistoryDetailById(applicationHistory);
        List<OptionResponseDto.HistoryOptionResponse> optionResponses = applicationHistory.getOptionList().stream()
                .map(optionMapper::toHistoryOptionDetailResponse)
                .collect(Collectors.toList());
        applicationHistoryDetailById.setOptions(optionResponses);
        return applicationHistoryDetailById;
    }

    public ApplicationHistory findApplicationHistory(int applicationHistoryId){
        return verifiedApplicationHistory(applicationHistoryId);
    }

    public ApplicationHistory patchApplicationHistory(ApplicationHistory applicationHistory){
        ApplicationHistory applicationHistory1 = findApplicationHistory(applicationHistory.getId());
        Optional.ofNullable(applicationHistory.getProgress()).ifPresent(applicationHistory1::setProgress);
        Optional.ofNullable(applicationHistory.getComment()).ifPresent(applicationHistory1::setComment);
        Optional.ofNullable(applicationHistory.getAddress()).ifPresent(applicationHistory1::setAddress);
        Optional.ofNullable(applicationHistory.getDetailAddress()).ifPresent(applicationHistory1::setDetailAddress);
        Optional.ofNullable(applicationHistory.getPhoto()).ifPresent(applicationHistory1::setPhoto);
        return applicationHistoryRepository.save(applicationHistory1);
    }

    public ApplicationHistory verifiedApplicationHistory(int applicationHistoryId){
        Optional<ApplicationHistory> applicationHistory = applicationHistoryRepository.findById(applicationHistoryId);
        return applicationHistory.orElse(null);
    }

    public void deleteApplicationHistory(int applicationHistoryId){
        ApplicationHistory applicationHistory = verifiedApplicationHistory(applicationHistoryId);
        applicationHistoryRepository.delete(applicationHistory);
    }
}
