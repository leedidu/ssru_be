package com.example.ssru.history.applicationhistory.mapper;

import com.example.ssru.history.applicationhistory.dto.ApplicationHistoryRequestDto;
import com.example.ssru.history.applicationhistory.dto.ApplicationHistoryResponseDto;
import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationHistoryMapper {
    ApplicationHistoryResponseDto.Response ApplicationHistoryResponseDto(ApplicationHistory applicationHistory);
    ApplicationHistoryResponseDto.ApplicationResponse ApplicationHistoryDetailResponse(ApplicationHistory application);
    ApplicationHistory ApplicationHistoryRequestPostDto(ApplicationHistoryRequestDto.Post post);
    ApplicationHistory ApplicationHistoryRequestPatchDto(ApplicationHistoryRequestDto.Patch patch);
    ApplicationHistoryResponseDto.ApplicationHistoryDetail toApplicationHistoryDetail(ApplicationHistory applicationHistory);
    ApplicationHistoryResponseDto.ApplicationHistoryDetailById toApplicationHistoryDetailById(ApplicationHistory applicationHistory);
}