package com.example.ssru.payment.service;

import com.example.ssru.payment.dto.IamportPaymentResponseDto;
import com.example.ssru.payment.dto.PaymentRequestDto;
import com.example.ssru.pointhistory.entity.PointHistory;
import com.example.ssru.pointhistory.repository.PointHistoryRepository;
import com.example.ssru.pointhistory.service.PointHistoryService;
import com.example.ssru.security.iamport.IamportTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    @Value("${iamport.base_url}")
    private String baseUrl;

    @Value("${iamport.imp}")
    private String imp;
    private final RestTemplate restTemplate;
    private final IamportTokenService iamportTokenService;
    private final PointHistoryService pointHistoryService;
    private PointHistoryRepository pointHistoryRepository;
    private PaymentService(RestTemplate restTemplate, IamportTokenService iamportTokenService, PointHistoryService pointHistoryService) { this.restTemplate = restTemplate;
        this.iamportTokenService = iamportTokenService;
        this.pointHistoryService = pointHistoryService;
    }

    public boolean postPreparePayment(PaymentRequestDto.prepareCheck prepareCheck) {
        System.out.println("----- 결제 사전 검증 등록 시작 -----");
        try {
            String url = baseUrl + "/payments/prepare";

            String token = iamportTokenService.getAccessTokenToIamport();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token); // Bearer 토큰으로 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("merchant_uid", prepareCheck.getMerchantUid()); // 가맹점 주문번호
            requestBody.put("amount", prepareCheck.getAmount()); // 결제 예정금액

            System.out.println("가맹점 주문번호 : " + prepareCheck.getMerchantUid() + "\n가격 : " + prepareCheck.getAmount());

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<IamportPaymentResponseDto.IamportPaymentPrepareResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, request, IamportPaymentResponseDto.IamportPaymentPrepareResponseDto.class);
            System.out.println("----- 결제 사전 검증 등록 성공 -----");
            return response.getBody().getResponse() != null;
        } catch (HttpClientErrorException e){
            System.out.println("----- 결제 사전 검증 등록 실패 -----");
            return false;
        }
    }

    public boolean checkPreparePayment(String uid){
        System.out.println("----- 결제 사전 검증 시작 -----");
        try {
            String url = baseUrl + "/payments/prepare/" + uid;

            String token = iamportTokenService.getAccessTokenToIamport();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token); // Bearer 토큰으로 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<IamportPaymentResponseDto.IamportPaymentPrepareResponseDto> response = restTemplate.exchange(url, HttpMethod.GET,request, IamportPaymentResponseDto.IamportPaymentPrepareResponseDto.class);

            System.out.println("----- 결제 사전 검증 완료 -----");

            return response.getBody().getCode() == 0;
        } catch (HttpClientErrorException e){
            System.out.println("----- 결제 사전 검증 실패 -----");
            return false;
        }
    }

    public boolean checkPaymentAndSave(String uid, PaymentRequestDto.prepareCheck prepareCheck){
        System.out.println("----- 결제 사후 검증 시작 -----");
        try {
            String url = baseUrl + "/payments/find/" + uid + "/paid";

            String token = iamportTokenService.getAccessTokenToIamport();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token); // Bearer 토큰으로 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<IamportPaymentResponseDto.IamportPaymentCheckResponseDto> response = restTemplate.exchange(url, HttpMethod.GET,request, IamportPaymentResponseDto.IamportPaymentCheckResponseDto.class);

            if(response.getBody().getCode() == 0){
                System.out.println("결제 사후 검증 완료 -> 유효한 결제임을 증명");
                if(response.getBody().getResponse().getMerchantUid().equals(uid) && response.getBody().getResponse().getAmount() == prepareCheck.getAmount()){
                    System.out.println("가맹점 주문번호 및 가격 비교 완료");
                    PointHistory pointHistory = new PointHistory();
                    pointHistory.setMerchantUid(uid);
                    pointHistory.setMethod(response.getBody().getResponse().getPgProvider());
                    pointHistory.setAmount(response.getBody().getResponse().getAmount());
                    pointHistory.setSituation("charge");

                    PointHistory savePointHistory = pointHistoryService.createPointHistory(pointHistory);
                    if(savePointHistory == null) return false;
                    System.out.println("----- 결제 사후 검증 완료 -----");
                    return true;
                }
            }
            System.out.println("----- 결제 사후 검증 실패 -----");
            System.out.println("사유 : 유효한 결제가 아니거나 가맹점 주문번호/가격 비교 실패");
            return false;
        } catch (HttpClientErrorException e){
            System.out.println("----- 결제 사후 검증 실패 -----");
            return false;
        }
    }
}
