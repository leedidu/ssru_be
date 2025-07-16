package com.example.ssru.account.service;

import com.example.ssru.account.dto.AccountRequestDto;
import com.example.ssru.account.dto.AccountResponseDto;
import com.example.ssru.account.entity.Account;
import com.example.ssru.account.mapper.AccountMapper;
import com.example.ssru.account.repository.AccountRepository;
import com.example.ssru.exception.CustomException;
import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.user.entity.User;
import com.example.ssru.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;
    private VerifyAccountService verifyAccountService;
    private AccountMapper accountMapper;
    private UserService userService;

    public Account createAccount(AccountRequestDto.Post post){
        System.out.println("----- 계좌 등록 실행 -----");
        User user = userService.getLoginUser();
        if(verifyAccountService.verifyAccountUser(post)){
            Account account = accountMapper.AccountRequestPostDto(post);
            account.setUser(user);
            System.out.println("계좌 ---> 계좌 정보 저장 완료");
            System.out.println("----- 계좌 등록 성공 -----");
            return accountRepository.save(account);
        }
        System.out.println("----- 계좌 등록 실패 -----");
        System.out.println("사유 : 계좌 검증 실패");
        return null;
    }

    public List<AccountResponseDto.accountListResponse> getAccountList(){
        System.out.println("----- 계좌 리스트 반환 시작 -----");
        User user = userService.getLoginUser();
        List<Account> accounts = accountRepository.findAllByUser(user);
        System.out.println("----- 계좌 리스트 가져오기 성공-----");
        return accounts.stream().map(account -> accountMapper.toAccountListResponseDto(account)).collect(Collectors.toList());
    }

    public Account findAccount(int accountId){
        return verifiedAccount(accountId);
    }

    public Account patchAccount(Account account){
        Account account1 = findAccount(account.getId());
        Optional.ofNullable(account.getBank()).ifPresent(account1::setBank);
        Optional.ofNullable(account.getAccount()).ifPresent(account1::setAccount);
        Optional.ofNullable(account.getName()).ifPresent(account1::setName);
        return accountRepository.save(account1);
    }

    public Account verifiedAccount(int accountId){
        Optional<Account> account = accountRepository.findById(accountId);
        return account.orElseThrow(() -> new CustomException(CustomExceptionCode.SSTEAL_HISTORY_NOT_FOUNT));
    }

    public void deleteAccount(int accountId){
        Account account1 = verifiedAccount(accountId);
        accountRepository.delete(account1);
    }
}
