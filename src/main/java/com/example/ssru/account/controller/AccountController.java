package com.example.ssru.account.controller;

import com.example.ssru.account.dto.AccountRequestDto;
import com.example.ssru.account.dto.AccountResponseDto;
import com.example.ssru.account.entity.Account;
import com.example.ssru.account.mapper.AccountMapper;
import com.example.ssru.account.service.AccountService;
import com.example.ssru.account.service.VerifyAccountService;
import com.example.ssru.exception.CustomException;
import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.exception.ExceptionResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final VerifyAccountService verifyAccountService;

    @PostMapping
    public ResponseEntity postAccount(@Valid @RequestBody AccountRequestDto.Post post) {
        Account account = accountService.createAccount(post);
        HttpStatus status = (account != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        if (account != null) {
            AccountResponseDto.Response response = new AccountResponseDto.Response("success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ExceptionResponseDto.Response responseBody = new ExceptionResponseDto.Response(
                    "fail",
                    CustomExceptionCode.ACCOUNT_REGISTER_FAIL.getCode(),
                    CustomExceptionCode.ACCOUNT_REGISTER_FAIL.getMessage()
            );
            return new ResponseEntity<>(responseBody, status);
        }
    }

    @GetMapping("/list")
    public ResponseEntity getAccountList(){
        List<AccountResponseDto.accountListResponse> accountListResponseList = accountService.getAccountList();
        return new ResponseEntity<>(accountListResponseList, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAccount(@Positive @RequestParam int accountId){
        Account account = accountService.findAccount(accountId);
        AccountResponseDto.Response response = accountMapper.AccountResponseDto(account);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity verifyAccountHolder(@Valid @RequestBody AccountRequestDto.Post accountInfo) {
        boolean check = verifyAccountService.verifyAccountUser(accountInfo);
        HttpStatus status = check ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new AccountResponseDto.checkResponse(check), status);
    }

    @PatchMapping("/{account-id}")
    public ResponseEntity patchAccount(@Positive @PathVariable("account-id") int accountId,
                                    @RequestBody AccountRequestDto.Patch patch){
        patch.setId(accountId);
        Account account = accountService.patchAccount(accountMapper.AccountRequestPatchDto(patch));
        AccountResponseDto.Response response = accountMapper.AccountResponseDto(account);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{account-id}")
    public ResponseEntity deleteAccount(@Positive @PathVariable("account-id") int accountId){
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
