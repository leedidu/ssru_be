package com.example.ssru.account.service;

import com.example.ssru.account.dto.AccountRequestDto;
import com.example.ssru.account.dto.IamportResponseDto;
import com.example.ssru.security.iamport.IamportTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class VerifyAccountService {

    @Value("${iamport.base_url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final IamportTokenService iamportTokenService;

    @Autowired
    public VerifyAccountService(RestTemplate restTemplate, IamportTokenService iamportTokenService) {
        this.restTemplate = restTemplate;
        this.iamportTokenService = iamportTokenService;
    }

    public boolean verifyAccountUser(AccountRequestDto.Post accountInfo){
        System.out.println("----- 계좌 검증 시작 -----");
        System.out.println("계좌 번호 : " + accountInfo.getAccount());
        try {
            String token = iamportTokenService.getAccessTokenToIamport();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token); // Bearer 토큰으로 설정
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String url = baseUrl + "/vbanks/holder?bank_code=" + accountInfo.getBank() + "&bank_num=" + accountInfo.getAccount();

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<IamportResponseDto.CheckAccountResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, IamportResponseDto.CheckAccountResponse.class);
            System.out.println("아임포트 계좌 응답 : " + response.getStatusCode());
            System.out.println("----- 계좌 검증 완료 -----");
            return response.getBody().getResponse() != null && response.getBody().getResponse().getBankHolder().equals(accountInfo.getName());
        } catch (HttpClientErrorException ex) {
            System.out.println("----- 계좌 검증 실패 -----");
            System.out.println("사유 : " + ex.getStatusCode());
            return false;
        }
    }
}
