package com.example.ssru.user.mapper;

import com.example.ssru.user.dto.UserRequestDto;
import com.example.ssru.user.dto.UserResponseDto;
import com.example.ssru.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto.UserResponse toUserResponseDto(User user); // 회원가입 결과에 넣을 것
    User UserRequestPostDto(UserRequestDto.Post post);
    User UserRequestPatchDto(UserRequestDto.Patch patch);
}