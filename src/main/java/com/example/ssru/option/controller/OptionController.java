package com.example.ssru.option.controller;

import com.example.ssru.option.dto.OptionRequestDto;
import com.example.ssru.option.dto.OptionResponseDto;
import com.example.ssru.option.entity.Option;
import com.example.ssru.option.mapper.OptionMapper;
import com.example.ssru.option.service.OptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/option")
public class OptionController {

    private final OptionService optionService;
    private final OptionMapper optionMapper;

    @PostMapping
    public ResponseEntity postOption(@Valid @RequestBody OptionRequestDto.Post post){
        Option option = optionService.createOption(optionMapper.OptionRequestPostDto(post));
        OptionResponseDto.Response response = optionMapper.OptionResponseDto(option);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOption(@Positive @RequestParam int optionId){
        Option option = optionService.findOption(optionId);
        OptionResponseDto.Response response = optionMapper.OptionResponseDto(option);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{option-id}")
    public ResponseEntity patchOption(@Positive @PathVariable("option-id") int optionId,
                                    @RequestBody OptionRequestDto.Patch patch){
        patch.setId(optionId);
        Option option = optionService.patchOption(optionMapper.OptionRequestPatchDto(patch));
        OptionResponseDto.Response response = optionMapper.OptionResponseDto(option);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{option-id}")
    public ResponseEntity deleteOption(@Positive @PathVariable("option-id") int optionId){
        optionService.deleteOption(optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
