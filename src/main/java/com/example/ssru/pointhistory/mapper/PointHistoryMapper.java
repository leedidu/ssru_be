package com.example.ssru.pointhistory.mapper;

import com.example.ssru.pointhistory.dto.PointHistoryRequestDto;
import com.example.ssru.pointhistory.dto.PointHistoryResponseDto;
import com.example.ssru.pointhistory.entity.PointHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PointHistoryMapper {
    PointHistoryResponseDto.Response PointHistoryResponseDto(PointHistory pointHistory);
    PointHistory PointHistoryRequestPostDto(PointHistoryRequestDto.Post post);
    PointHistory PointHistoryRequestPatchDto(PointHistoryRequestDto.Patch patch);
    PointHistoryResponseDto.PointHistoryListResponse toPointHistoryListResponseDto(PointHistory pointHistory);
}