package com.example.ssru.account.mapper;

import com.example.ssru.account.dto.AccountRequestDto;
import com.example.ssru.account.dto.AccountResponseDto;
import com.example.ssru.account.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponseDto.Response AccountResponseDto(Account account);
    Account AccountRequestPostDto(AccountRequestDto.Post post);
    Account AccountRequestPatchDto(AccountRequestDto.Patch patch);
    AccountResponseDto.accountListResponse toAccountListResponseDto(Account account);
}