package com.example.ssru.option.service;

import com.example.ssru.exception.CustomException;
import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.option.dto.OptionResponseDto;
import com.example.ssru.option.entity.Option;
import com.example.ssru.option.mapper.OptionMapper;
import com.example.ssru.option.repository.OptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OptionService {

    private OptionRepository optionRepository;
    private OptionMapper optionMapper;

    public Option createOption(Option option){
        return optionRepository.save(option);
    }

    public Option findOption(int optionId){
        return verifiedOption(optionId);
    }

    public List<OptionResponseDto.OptionDetailResponse> getOptionInfoList(int applicationId){
        List<Option> options = optionRepository.findAllByApplicationHistoryId(applicationId);
        List<OptionResponseDto.OptionDetailResponse> optionDetailResponses = new ArrayList<>();
        for(Option option : options){
            optionDetailResponses.add(optionMapper.toOptionDetailResponse(option));
        }
        return optionDetailResponses;
    }

    public Option patchOption(Option option){
        Option option1 = findOption(option.getId());
        Optional.ofNullable(option.getType()).ifPresent(option1::setType);
        Optional.ofNullable(option.getDetail()).ifPresent(option1::setDetail);
        if(option.getCount() != 0) option1.setCount(option.getCount());
        if(option.getPrice() != 0) option1.setPrice(option.getPrice());
        return optionRepository.save(option1);
    }

    public Option verifiedOption(int optionId){
        Optional<Option> option = optionRepository.findById(optionId);
        return option.orElseThrow(() -> new CustomException(CustomExceptionCode.SSTEAL_HISTORY_NOT_FOUNT));
    }

    public void deleteOption(int optionId){
        Option option1 = verifiedOption(optionId);
        optionRepository.delete(option1);
    }
}