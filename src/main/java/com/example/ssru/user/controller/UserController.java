package com.example.ssru.user.controller;

import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.exception.ExceptionResponseDto;
import com.example.ssru.security.jwt.JwtToken;
import com.example.ssru.user.dto.UserRequestDto;
import com.example.ssru.user.dto.UserResponseDto;
import com.example.ssru.user.entity.User;
import com.example.ssru.user.mapper.UserMapper;
import com.example.ssru.user.repository.UserRepository;
import com.example.ssru.user.service.UserService;
import com.example.ssru.user.verfication.phone.PhoneService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;


import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

@RestController
@Validated
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PhoneService phoneService;

    @PostMapping("/sign-in")
    public ResponseEntity postUser(@Valid @RequestBody UserRequestDto.Post post){
        User user = userService.createUser(userMapper.UserRequestPostDto(post));
        UserResponseDto.UserResponse userResponse = userMapper.toUserResponseDto(user);
        UserResponseDto.Response response = new UserResponseDto.Response("success", userResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/check-id")
    public ResponseEntity checkIdDuplication(@RequestParam("loginId") String loginId){
        Boolean check = userService.checkDuplicateLoginId(loginId);
        UserResponseDto.DuplicationResponse duplicationResponse = new UserResponseDto.DuplicationResponse(check);
        return new ResponseEntity<>(duplicationResponse, HttpStatus.OK);
    }

    @GetMapping("/check-email")
    public ResponseEntity checkEmailDuplication(@RequestParam("email") String email){
        Boolean check = userService.checkDuplicateEmail(email);
        UserResponseDto.DuplicationResponse duplicationResponse = new UserResponseDto.DuplicationResponse(check);
        return new ResponseEntity<>(duplicationResponse, HttpStatus.OK);
    }

    @GetMapping("/check-phone")
    public ResponseEntity checkPhoneDuplication(@RequestParam("phone") String phone){
        Boolean check = userService.checkDuplicatePhone(phone);
        UserResponseDto.DuplicationResponse duplicationResponse = new UserResponseDto.DuplicationResponse(check);
        return new ResponseEntity<>(duplicationResponse, HttpStatus.OK);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity checkNicknameDuplication(@RequestParam("nickname") String nickname){
        Boolean check = userService.checkDuplicateNickname(nickname);
        UserResponseDto.DuplicationResponse duplicationResponse = new UserResponseDto.DuplicationResponse(check);
        return new ResponseEntity<>(duplicationResponse, HttpStatus.OK);
    }

    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendEmail(@RequestParam("email") @Valid @Email String email) {
        userService.sendCodeToEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/emails/verifications")
    public ResponseEntity verificationEmail(@RequestParam("email") @Valid @Email String email,
                                            @RequestParam("code") String authCode) {
        boolean response = userService.verifiedCode(email, authCode);
        UserResponseDto.VerifyCheck check = new UserResponseDto.VerifyCheck(response ? "success" : "fail");
        return new ResponseEntity<>(check, HttpStatus.OK);
    }

    @PostMapping("/phone/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("phone") @Valid String phone) {
        phoneService.sendSms(phone);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/phone/verifications")
    public ResponseEntity verificationPhone(@RequestParam("phone") @Valid String phone,
                                            @RequestParam("code") String authCode) {
        boolean response = userService.verifiedCode(phone, authCode);
        UserResponseDto.VerifyCheck check = new UserResponseDto.VerifyCheck(response ? "success" : "fail");
        return new ResponseEntity<>(check, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserRequestDto.Login login, HttpServletResponse httpServletResponse){
        JwtToken token = userService.loginUser(login.getLoginId(), login.getLoginPw());
        if(token != null){
            User user = userRepository.findByLoginId(login.getLoginId());
            UserResponseDto.UserResponse userResponse = userMapper.toUserResponseDto(user);
            httpServletResponse.setHeader("Authorization", token.getAccessToken()); //헤더에 토큰 추가해서 반환하기 위한 용도
            httpServletResponse.setHeader("Refresh-Token", token.getRefreshToken());
            UserResponseDto.Response response = new UserResponseDto.Response("success", userResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else{
            CustomExceptionCode customExceptionCode = CustomExceptionCode.LOGIN_ERROR;
            ExceptionResponseDto.Response response = new ExceptionResponseDto.Response("fail", customExceptionCode.getCode(), customExceptionCode.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    public ResponseEntity getUser(@Positive @RequestParam int userId){
        User user = userService.findUserByUserId(userId);
        UserResponseDto.UserResponse response = userMapper.toUserResponseDto(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/user/info/edit")
    public ResponseEntity patchPost(@RequestBody UserRequestDto.Patch patch){
        boolean check = userService.patchUser(userMapper.UserRequestPatchDto(patch));
        if(check){
            User user = userService.getLoginUser();
            UserResponseDto.UserResponse userResponse = userMapper.toUserResponseDto(user);
            UserResponseDto.Response response = new UserResponseDto.Response("success", userResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else{
            CustomExceptionCode customExceptionCode = CustomExceptionCode.EDIT_INFO_FAIL;
            ExceptionResponseDto.Response response = new ExceptionResponseDto.Response("fail", customExceptionCode.getCode(), customExceptionCode.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/{user-id}")
    public ResponseEntity deletePost(@Positive @PathVariable("user-id") int postId){
        userService.deleteUser(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
