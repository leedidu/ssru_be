package com.example.ssru.pointhistory.controller;

import com.example.ssru.pointhistory.dto.PointHistoryRequestDto;
import com.example.ssru.pointhistory.dto.PointHistoryResponseDto;
import com.example.ssru.pointhistory.entity.PointHistory;
import com.example.ssru.pointhistory.mapper.PointHistoryMapper;
import com.example.ssru.pointhistory.service.PointHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/point-history")
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;
    private final PointHistoryMapper pointHistoryMapper;

    @PostMapping
    public ResponseEntity postPointHistory(@Valid  @RequestBody PointHistoryRequestDto.Post post){
        PointHistory pointHistory = pointHistoryService.createPointHistory(pointHistoryMapper.PointHistoryRequestPostDto(post));
        PointHistoryResponseDto.Response response = pointHistoryMapper.PointHistoryResponseDto(pointHistory);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{situation}")
    public ResponseEntity getPointHistoryList(@PathVariable("situation") String situation){
        List<PointHistoryResponseDto.PointHistoryListResponse> pointHistory = pointHistoryService.getPointHistoryList(situation);
        return new ResponseEntity<>(pointHistory, HttpStatus.OK);
    }

    @GetMapping("/{situation}/{startDate}-to-{endDate}")
    public ResponseEntity getPointHistoryListByDate(@PathVariable("situation") String situation,
                                                    @PathVariable("startDate") String startDate,
                                                    @PathVariable("endDate") String endDate){
        List<PointHistoryResponseDto.PointHistoryListResponse> pointHistory = pointHistoryService.getPointHistoryListByDate(situation, startDate, endDate);
        return new ResponseEntity<>(pointHistory, HttpStatus.OK);
    }

    @PatchMapping("/{pointHistory-id}")
    public ResponseEntity patchPointHistory(@Positive @PathVariable("pointHistory-id") int pointHistoryId,
                                    @RequestBody PointHistoryRequestDto.Patch patch){
        patch.setId(pointHistoryId);
        PointHistory pointHistory = pointHistoryService.patchPointHistory(pointHistoryMapper.PointHistoryRequestPatchDto(patch));
        PointHistoryResponseDto.Response response = pointHistoryMapper.PointHistoryResponseDto(pointHistory);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{pointHistory-id}")
    public ResponseEntity deletePointHistory(@Positive @PathVariable("pointHistory-id") int pointHistoryId){
        pointHistoryService.deletePointHistory(pointHistoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
