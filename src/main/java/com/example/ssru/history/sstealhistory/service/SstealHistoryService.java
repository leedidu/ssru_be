package com.example.ssru.history.sstealhistory.service;

import com.example.ssru.exception.CustomException;
import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.history.applicationhistory.dto.ApplicationHistoryResponseDto;
import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.applicationhistory.repository.ApplicationHistoryRepository;
import com.example.ssru.history.applicationhistory.service.ApplicationHistoryService;
import com.example.ssru.history.sstealhistory.dto.SstealHistoryRequestDto;
import com.example.ssru.history.sstealhistory.dto.SstealHistoryResponseDto;
import com.example.ssru.history.sstealhistory.entity.SstealHistory;
import com.example.ssru.history.sstealhistory.mapper.SstealHistoryMapper;
import com.example.ssru.history.sstealhistory.repository.SstealHistoryRepository;
import com.example.ssru.option.dto.OptionResponseDto;
import com.example.ssru.option.entity.Option;
import com.example.ssru.option.mapper.OptionMapper;
import com.example.ssru.option.repository.OptionRepository;
import com.example.ssru.option.service.OptionService;
import com.example.ssru.pointhistory.entity.PointHistory;
import com.example.ssru.pointhistory.repository.PointHistoryRepository;
import com.example.ssru.pointhistory.service.PointHistoryService;
import com.example.ssru.s3.S3UploadService;
import com.example.ssru.user.entity.User;
import com.example.ssru.user.service.UserService;
import com.siot.IamportRestClient.App;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SstealHistoryService {

    private SstealHistoryRepository sstealHistoryRepository;
    private SstealHistoryMapper sstealHistoryMapper;
    private UserService userService;
    private OptionRepository optionRepository;
    private OptionService optionService;
    private ApplicationHistoryService applicationHistoryService;
    private OptionMapper optionMapper;
    private ApplicationHistoryRepository applicationHistoryRepository;
    private PointHistoryService pointHistoryService;
    private PointHistoryRepository pointHistoryRepository;
    private S3UploadService s3UploadService;

    public SstealHistory createSstealHistory(SstealHistoryRequestDto.Post post){
        System.out.println("----- 쓰틸 신청 시작 -----");

        User user = userService.getLoginUser();
        boolean checkOption = post.getOptionId().stream()
                .map(optionService::findOption)
                .allMatch(option -> option.getProgress().equals("신청중"));

        if(user.getAuth().equals("ROLE_SSTEALER") && checkOption){
            SstealHistory sstealHistory = sstealHistoryMapper.SstealHistoryRequestPostDto(post);
            sstealHistory.setSstealer(user);

            SstealHistory savedSstealHistory = sstealHistoryRepository.save(sstealHistory);

            List<Option> options = post.getOptionId().stream()
                    .map(optionService::findOption) // 각 optionId에 대해 optionService.findOption()을 수행하여 Option 객체 반환
                    .filter(Objects::nonNull)      // Option 객체가 null이 아닌 경우만 필터링
                    .collect(Collectors.toList());  // 결과를 List<Option>으로 수집

            savedSstealHistory.setOptionList(options);

            options.forEach(option -> {
                option.setProgress("쓰틸중");
                option.setSstealHistory(sstealHistory); // Set the association
                optionRepository.save(option);
            });

            int applicationId = options.get(0).getApplicationHistory().getId();
            ApplicationHistory applicationHistory = applicationHistoryService.findApplicationHistory(applicationId);
            List<Option> optionList = applicationHistory.getOptionList();

            if(optionList.stream().allMatch(option -> "쓰틸중".equals(option.getProgress()) || "완료".equals(option.getProgress()))){
                applicationHistory.setProgress("대기중");
                applicationHistoryRepository.save(applicationHistory);
            }

            return savedSstealHistory;
        }
        System.out.println("----- 유저의 권한이 쓰틸러가 아님 : 널 반환 -----");
        return null;
    }

    public SstealHistory completeSsteal(int sstealId, MultipartFile photo) throws IOException {
        System.out.println("----- 쓰틸 완료 시작 -----");
        User user = userService.getLoginUser();
        SstealHistory existSsteal = findSstealHistory(sstealId);

        if(user.getId() ==  existSsteal.getSstealer().getId()){ // 로그인한 유저와 쓰틸러가 같을 경우에만
            List<Option> options = optionRepository.findAllBySstealHistoryId(sstealId);

            options.forEach(option -> {
                option.setProgress("완료");
                optionRepository.save(option);
            });

            int applicationId = options.get(0).getApplicationHistory().getId();
            ApplicationHistory applicationHistory = applicationHistoryService.findApplicationHistory(applicationId);
            List<Option> optionList = applicationHistory.getOptionList();

            if(optionList.stream().allMatch(option -> "완료".equals(option.getProgress()))){
                applicationHistory.setProgress("완료");
                applicationHistoryRepository.save(applicationHistory);
            }
            existSsteal.setProgress("완료");

            System.out.println("-- 사진 저장 시작");
            String savePhoto = s3UploadService.saveFile(photo);
            existSsteal.setPhoto(savePhoto);
            sstealHistoryRepository.save(existSsteal);
            System.out.println("-- 사진 저장 완료");

            int point = user.getPoint() + options.stream().mapToInt(option -> option.getPrice() * option.getCount()).sum();
            user.setPoint(point); // 수거하는 만큼 포인트 추가

            PointHistory pointHistory = new PointHistory();
            pointHistory.setSituation("accumulate");
            pointHistory.setAmount(existSsteal.getOptionList().get(0).getApplicationHistory().getPrice());
            pointHistoryService.createPointHistory(pointHistory);
            pointHistory.setSstealHistory(existSsteal);
            pointHistoryRepository.save(pointHistory);
            System.out.println("쓰틸 완료 후 유저의 포인트 : " + point);
            System.out.println("포인트 내역 번호 : " + pointHistory.getId());
            System.out.println("----- 쓰틸 완료 성공 -----");
            return existSsteal;
        }
        System.out.println("----- 쓰틸 완료 실패 -----");
        System.out.println("사유 : 로그인한 유저와 쓰틸러가 동일하지 않음");
        return null;
    }

    public boolean cancelSsteal(int sstealId, SstealHistoryRequestDto.CancelSsteal cancelSsteal){
        System.out.println("----- 쓰틸 취소 시작 -----");
        SstealHistory sstealHistory = findSstealHistory(sstealId);
        if(sstealHistory != null && cancelSsteal.getReason() != null){
            List<Option> options = sstealHistory.getOptionList();

            options.forEach(option -> {
                option.setProgress("취소");
                optionRepository.save(option);
            });
            sstealHistory.setCancelReason(cancelSsteal.getReason());
            sstealHistory.setProgress("취소");

            sstealHistoryRepository.save(sstealHistory);
            System.out.println("----- 쓰틸 취소 완료 -----");
            return true;
        }
        System.out.println("----- 쓰틸 취소 실패 -----");
        System.out.println("사유 : 쓰틸내역을 찾지 못하거나 취소사유가 널값");
        return false;
    }

    public List<ApplicationHistoryResponseDto.ApplicationHistoryDetail> getSstealHistoryList(String progress) {
        User user = userService.getLoginUser();
        Map<String, String> progressMap = new HashMap<String, String>() {{
            put("all", "전체");
            put("ssteal-ing", "쓰틸중");
            put("completed", "완료");
        }};

        String mappedProgress = progressMap.get(progress.trim());

        List<ApplicationHistory> applicationHistories = new ArrayList<>();
        if(mappedProgress.equals("전체")){
            applicationHistories = sstealHistoryRepository.findAllBySstealerOrderByCreatedDesc(user)
                    .stream()
                    .flatMap(sstealHistory -> sstealHistory.getOptionList().stream().map(Option::getApplicationHistory))
                    .collect(Collectors.toList());
        } else{
            applicationHistories = sstealHistoryRepository.findAllBySstealerAndProgressOrderByCreatedDesc(user, mappedProgress)
                    .stream()
                    .flatMap(sstealHistory -> sstealHistory.getOptionList().stream().map(Option::getApplicationHistory))
                    .collect(Collectors.toList());
        }
        for(ApplicationHistory applicationHistory : applicationHistories){
            if(applicationHistory.getProgress().equals("완료")){
                applicationHistory.setDetailAddress(null);
            }
        }
        return applicationHistoryService.mapToHistoryDetails(applicationHistories);
    }

    public SstealHistoryResponseDto.SstealHistoryDetailById getSstealDetailInfoById(int applicationId){
        SstealHistory sstealHistory = findSstealHistory(applicationId);
        return mapToSstealHistoryDetail(sstealHistory);
    }

    public SstealHistoryResponseDto.SstealHistoryDetailById mapToSstealHistoryDetail(SstealHistory sstealHistory) {
        String address = sstealHistory.getOptionList().get(0).getApplicationHistory().getAddress();
        String detailAddress = sstealHistory.getOptionList().get(0).getApplicationHistory().getDetailAddress();

        int price = sstealHistory.getOptionList().stream()
                .mapToInt(option -> option.getPrice() * option.getCount())
                .sum();

        List<OptionResponseDto.HistoryOptionResponse> optionResponses = sstealHistory.getOptionList().stream()
                .map(optionMapper::toHistoryOptionDetailResponse)
                .collect(Collectors.toList());

        if(sstealHistory.getProgress().equals("완료")){
            return new SstealHistoryResponseDto.SstealHistoryDetailById(sstealHistory.getId(), address, null, sstealHistory.getUpdated(), price, sstealHistory.getPhoto(), optionResponses);
        } else{
            return new SstealHistoryResponseDto.SstealHistoryDetailById(sstealHistory.getId(), address, detailAddress, sstealHistory.getUpdated(), price, sstealHistory.getPhoto(), optionResponses);
        }
    }

    public SstealHistory findSstealHistory(int sstealHistoryId){
        return verifiedSstealHistory(sstealHistoryId);
    }

    public SstealHistory patchSstealHistory(SstealHistory sstealHistory){
        SstealHistory sstealHistory1 = findSstealHistory(sstealHistory.getId());
        Optional.ofNullable(sstealHistory.getPhoto()).ifPresent(sstealHistory1::setPhoto);
        Optional.ofNullable(sstealHistory.getEstimatedDate()).ifPresent(sstealHistory1::setEstimatedDate);
        Optional.ofNullable(sstealHistory.getProgress()).ifPresent(sstealHistory1::setProgress);
        return sstealHistoryRepository.save(sstealHistory1);
    }

    public SstealHistory verifiedSstealHistory(int sstealHistoryId){
        Optional<SstealHistory> sstealHistory = sstealHistoryRepository.findById(sstealHistoryId);
        return sstealHistory.orElse(null);
    }

    public void deleteSstealHistory(int sstealHistoryId){
        SstealHistory sstealHistory1 = verifiedSstealHistory(sstealHistoryId);
        sstealHistoryRepository.delete(sstealHistory1);
    }
}
