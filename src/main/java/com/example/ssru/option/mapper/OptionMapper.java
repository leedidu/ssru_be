package com.example.ssru.option.mapper;

import com.example.ssru.address.dto.AddressResponseDto;
import com.example.ssru.address.entity.Address;
import com.example.ssru.history.sstealhistory.dto.SstealHistoryResponseDto;
import com.example.ssru.option.dto.OptionRequestDto;
import com.example.ssru.option.dto.OptionResponseDto;
import com.example.ssru.option.entity.Option;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OptionMapper {
    OptionResponseDto.Response OptionResponseDto(Option option);
    Option OptionRequestPostDto(OptionRequestDto.Post post);
    Option OptionRequestPatchDto(OptionRequestDto.Patch patch);
    OptionResponseDto.OptionDetailResponse toOptionDetailResponse(Option option);
    default OptionResponseDto.HistoryOptionResponse toHistoryOptionDetailResponse(Option option) {
        return new OptionResponseDto.HistoryOptionResponse(
                option.getType(),
                option.getDetail(),
                option.getCount()
        );
    }
}