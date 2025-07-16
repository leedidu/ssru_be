package com.example.ssru.security.iamport;

import com.example.ssru.account.dto.IamportResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class IamportTokenService {

    @Value("${iamport.base_url}")
    private String baseUrl;

    @Value("${iamport.imp_key}")
    private String impKey;

    @Value("${iamport.imp_secret}")
    private String impSecret;

    private final RestTemplate restTemplate;

    public IamportTokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAccessTokenToIamport() {
        System.out.println("----- 아임포트 토큰 반환 시작 -----");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("imp_key", impKey);
        formData.add("imp_secret", impSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        // 아임포트로 요청 보내기
        String url = baseUrl + "/users/getToken";
        ResponseEntity<IamportResponseDto.IamPortResponse> response = restTemplate.postForEntity(url, request, IamportResponseDto.IamPortResponse.class);

        // 응답 받은 객체에서 access_token 추출
        if (response.getStatusCode().is2xxSuccessful()) {
            IamportResponseDto.IamPortResponse iamportResponse = response.getBody();
            if (iamportResponse != null && iamportResponse.getResponse() != null) {
                System.out.println("----- 아임포트 토큰 반환 성공 -----");
                return iamportResponse.getResponse().getAccessToken(); // access_token 반환
            }
        }
        System.out.println("----- 아임포트 토큰 반환 실패 -----");
        return null;
    }
}
