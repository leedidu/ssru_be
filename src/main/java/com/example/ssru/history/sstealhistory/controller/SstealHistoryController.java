package com.example.ssru.history.sstealhistory.controller;

import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.exception.ExceptionResponseDto;
import com.example.ssru.history.applicationhistory.dto.ApplicationHistoryResponseDto;
import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.sstealhistory.dto.SstealHistoryRequestDto;
import com.example.ssru.history.sstealhistory.dto.SstealHistoryResponseDto;
import com.example.ssru.history.sstealhistory.entity.SstealHistory;
import com.example.ssru.history.sstealhistory.mapper.SstealHistoryMapper;
import com.example.ssru.history.sstealhistory.service.SstealHistoryService;
import com.example.ssru.option.dto.OptionResponseDto;
import com.example.ssru.option.service.OptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/ssteal")
public class SstealHistoryController {
    
    private final SstealHistoryService sstealHistoryService;
    private final SstealHistoryMapper sstealHistoryMapper;
    private final OptionService optionService;

    @PostMapping
    public ResponseEntity postSstealHistory(@Valid @RequestBody SstealHistoryRequestDto.Post post){
        SstealHistory sstealHistory = sstealHistoryService.createSstealHistory(post);
        HttpStatus status = (sstealHistory != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        Object responseBody = (sstealHistory != null)
                ? new SstealHistoryResponseDto.Response("success", sstealHistoryMapper.SstealHistoryDetailResponseDto(sstealHistory))
                : new ExceptionResponseDto.Response("fail", CustomExceptionCode.SSTEAL_FAIL.getCode(), CustomExceptionCode.SSTEAL_FAIL.getMessage());

        return new ResponseEntity<>(responseBody, status);
    }

    @PostMapping("/{ssteal-id}")
    public ResponseEntity completeSsteal(@Positive @PathVariable("ssteal-id") int sstealId,
                                         @RequestParam("photo") MultipartFile photo) throws IOException {
        SstealHistory sstealHistory = sstealHistoryService.completeSsteal(sstealId, photo);
        HttpStatus status = (sstealHistory != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        Object responseBody = (sstealHistory != null)
                ? new SstealHistoryResponseDto.Response("success", sstealHistoryMapper.SstealHistoryDetailResponseDto(sstealHistory))
                : new ExceptionResponseDto.Response("fail", CustomExceptionCode.COMPLETE_SSTEAL_FAIL.getCode(), CustomExceptionCode.COMPLETE_SSTEAL_FAIL.getMessage());

        return new ResponseEntity<>(responseBody, status);
    };

    @GetMapping("/{progress}")
    public ResponseEntity getSstealHistoryList(@PathVariable("progress") String progress){
        List<ApplicationHistoryResponseDto.ApplicationHistoryDetail> sstealHistoryDetails = sstealHistoryService.getSstealHistoryList(progress);
        return new ResponseEntity<>(sstealHistoryDetails, HttpStatus.OK);
    }

    @GetMapping("/detail/{ssteal-id}")
    public ResponseEntity getSstealHistoryDetail(@Positive @PathVariable("ssteal-id") int sstealId){
        return new ResponseEntity<>(sstealHistoryService.getSstealDetailInfoById(sstealId), HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{ssteal-id}")
    public ResponseEntity cancelSsteal(@Positive @PathVariable("ssteal-id") int sstealId,
                                       @Valid @RequestBody SstealHistoryRequestDto.CancelSsteal cancelSsteal){
        boolean cancelCheck = sstealHistoryService.cancelSsteal(sstealId, cancelSsteal);
        HttpStatus status = cancelCheck ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        Object response = cancelCheck
                ? new SstealHistoryResponseDto.CancelResponse("success")
                : new ExceptionResponseDto.Response("fail", CustomExceptionCode.CANCEL_SSTEAL_FAIL.getCode(), CustomExceptionCode.CANCEL_SSTEAL_FAIL.getMessage());

        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/{application-id}/list")
    public ResponseEntity getOptionInfoList(@Positive @PathVariable("application-id") int applicationId){
        List<OptionResponseDto.OptionDetailResponse> optionDetailResponses = optionService.getOptionInfoList(applicationId);
        return new ResponseEntity<>(optionDetailResponses, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getSstealHistory(@Positive @RequestParam int sstealHistoryId){
        SstealHistory sstealHistory = sstealHistoryService.findSstealHistory(sstealHistoryId);
        SstealHistoryResponseDto.Response response = sstealHistoryMapper.SstealHistoryResponseDto(sstealHistory);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{SstealHistory-id}")
    public ResponseEntity patchSstealHistory(@Positive @PathVariable("SstealHistory-id") int sstealHistoryId,
                                            @RequestBody SstealHistoryRequestDto.Patch patch){
        patch.setId(sstealHistoryId);
        SstealHistory sstealHistory = sstealHistoryService.patchSstealHistory(sstealHistoryMapper.SstealHistoryRequestPatchDto(patch));
        SstealHistoryResponseDto.Response response = sstealHistoryMapper.SstealHistoryResponseDto(sstealHistory);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{SstealHistory-id}")
    public ResponseEntity deleteSstealHistory(@Positive @PathVariable("SstealHistory-id") int sstealHistoryId){
        sstealHistoryService.deleteSstealHistory(sstealHistoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
