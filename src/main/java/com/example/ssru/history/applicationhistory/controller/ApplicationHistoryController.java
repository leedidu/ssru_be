package com.example.ssru.history.applicationhistory.controller;

import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.exception.ExceptionResponseDto;
import com.example.ssru.history.applicationhistory.dto.ApplicationHistoryRequestDto;
import com.example.ssru.history.applicationhistory.dto.ApplicationHistoryResponseDto;
import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.applicationhistory.mapper.ApplicationHistoryMapper;
import com.example.ssru.history.applicationhistory.service.ApplicationHistoryService;
import com.example.ssru.history.sstealhistory.dto.SstealHistoryResponseDto;
import com.example.ssru.option.dto.OptionRequestDto;
import com.example.ssru.option.entity.Option;
import com.example.ssru.option.mapper.OptionMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/application")
public class ApplicationHistoryController {

    private final ApplicationHistoryService applicationHistoryService;
    private final ApplicationHistoryMapper applicationHistoryMapper;
    private final OptionMapper optionMapper;

    @PostMapping
    public ResponseEntity postApplicationHistory(@Valid @RequestBody ApplicationHistoryRequestDto.Post post){
        List<Option> options = post.getOption().stream()
                .map(optionMapper::OptionRequestPostDto)
                .collect(Collectors.toList());

        ApplicationHistory applicationHistory = applicationHistoryService.createApplicationHistory(applicationHistoryMapper.ApplicationHistoryRequestPostDto(post), options);
        HttpStatus status = (applicationHistory != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        Object responseBody = (applicationHistory != null)
                ? new ApplicationHistoryResponseDto.Response("success", applicationHistoryMapper.ApplicationHistoryDetailResponse(applicationHistory))
                : new ExceptionResponseDto.Response("fail", CustomExceptionCode.POINT_SHORTAGE.getCode(), CustomExceptionCode.POINT_SHORTAGE.getMessage());

        return new ResponseEntity<>(responseBody, status);
    }

    @DeleteMapping("/cancel/{application-id}")
    public ResponseEntity cancelApplication(@Positive @PathVariable("application-id") int applicationId){
        boolean cancelCheck = applicationHistoryService.cancelApplication(applicationId);
        HttpStatus status = cancelCheck ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        Object response = cancelCheck
                ? new SstealHistoryResponseDto.CancelResponse("success")
                : new ExceptionResponseDto.Response("fail", CustomExceptionCode.APPLICATION_SSTEAL_FAIL.getCode(), CustomExceptionCode.APPLICATION_SSTEAL_FAIL.getMessage());

        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/detail/{application-id}")
    public ResponseEntity getApplicationHistoryDetail(@Positive @PathVariable("application-id") int applicationId){
        return new ResponseEntity<>(applicationHistoryService.getApplicationDetailInfoById(applicationId), HttpStatus.OK);
    }

    @PostMapping("/upload/{application-id}")
    public void uploadApplicationPhoto(@PathVariable("application-id") int id,
                                       @RequestParam("photo") MultipartFile photo) throws IOException {
        applicationHistoryService.uploadPhoto(id, photo);
    }

    @GetMapping
    public ResponseEntity getApplicationHistory(@Positive @RequestParam int applicationHistoryId){
        ApplicationHistory applicationHistory = applicationHistoryService.findApplicationHistory(applicationHistoryId);
        ApplicationHistoryResponseDto.Response response = applicationHistoryMapper.ApplicationHistoryResponseDto(applicationHistory);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{progress}")
    public ResponseEntity getApplicationHistoryList(@PathVariable("progress") String progress){
        List<ApplicationHistoryResponseDto.ApplicationHistoryDetail> applicationHistoryDetails = applicationHistoryService.getApplicationHistoryList(progress);
        return new ResponseEntity<>(applicationHistoryDetails, HttpStatus.OK);
    }

    @PatchMapping("/{applicationHistory-id}")
    public ResponseEntity patchApplicationHistory(@Positive @PathVariable("applicationHistory-id") int applicationHistoryId,
                                    @RequestBody ApplicationHistoryRequestDto.Patch patch){
        patch.setId(applicationHistoryId);
        ApplicationHistory applicationHistory = applicationHistoryService.patchApplicationHistory(applicationHistoryMapper.ApplicationHistoryRequestPatchDto(patch));
        ApplicationHistoryResponseDto.Response response = applicationHistoryMapper.ApplicationHistoryResponseDto(applicationHistory);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{applicationHistory-id}")
    public ResponseEntity deleteApplicationHistory(@Positive @PathVariable("applicationHistory-id") int applicationHistoryId){
        applicationHistoryService.deleteApplicationHistory(applicationHistoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
