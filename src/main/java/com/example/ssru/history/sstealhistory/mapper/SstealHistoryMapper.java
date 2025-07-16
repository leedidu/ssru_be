package com.example.ssru.history.sstealhistory.mapper;

import com.example.ssru.address.dto.AddressResponseDto;
import com.example.ssru.address.entity.Address;
import com.example.ssru.history.applicationhistory.dto.ApplicationHistoryResponseDto;
import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.sstealhistory.dto.SstealHistoryRequestDto;
import com.example.ssru.history.sstealhistory.dto.SstealHistoryResponseDto;
import com.example.ssru.history.sstealhistory.entity.SstealHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SstealHistoryMapper {
    SstealHistoryResponseDto.Response SstealHistoryResponseDto(SstealHistory sstealDetail);
    SstealHistoryResponseDto.SstealDetail SstealHistoryDetailResponseDto(SstealHistory sstealDetail);
    SstealHistory SstealHistoryRequestPostDto(SstealHistoryRequestDto.Post post);
    SstealHistory SstealHistoryRequestPatchDto(SstealHistoryRequestDto.Patch patch);
    SstealHistoryResponseDto.SstealHistoryDetailById toSstealHistoryDetailById(SstealHistory sstealHistory);
}