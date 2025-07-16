package com.example.ssru.security.jwt;

import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.exception.ExceptionResponseDto;
import com.example.ssru.user.dto.UserResponseDto;
import com.example.ssru.user.entity.User;
import com.example.ssru.user.mapper.UserMapper;
import com.example.ssru.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Validated
@AllArgsConstructor
public class TokenController {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/refresh")
    public ResponseEntity reissueToken(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String refreshToken = request.getHeader("Refresh-Token");
        User user = userService.getLoginUser();

        if(refreshToken.equals(user.getRefreshToken()) && tokenProvider.validateToken(refreshToken)){
            String accessToken = tokenProvider.createAccessToken(user.getLoginId(), user.getAuth());
            httpServletResponse.setHeader("Authorization", accessToken); //헤더에 토큰 추가해서 반환하기 위한 용도
            httpServletResponse.setHeader("Refresh-Token", user.getRefreshToken());
            UserResponseDto.UserResponse userResponse = userMapper.toUserResponseDto(user);
            UserResponseDto.Response response = new UserResponseDto.Response("success", userResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else{
            CustomExceptionCode customExceptionCode = CustomExceptionCode.REFRESH_TOKEN_IS_NOT_VALID;
            ExceptionResponseDto.Response response = new ExceptionResponseDto.Response("fail", customExceptionCode.getCode(), customExceptionCode.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
