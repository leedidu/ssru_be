package com.example.ssru.user.verfication.email;

import lombok.Setter;

@Setter
public class EmailVerificationResult {
    private boolean verified;
    private String message;
    private int code;

    // 생성자, Getter, Setter, toString 등을 포함할 수 있습니다.

    // 정적 팩토리 메서드
    public static EmailVerificationResult of(boolean verified) {
        EmailVerificationResult result = new EmailVerificationResult();
        result.setVerified(verified);
        result.setMessage(verified ? "Email verification succeeded" : "Email verification failed");
        result.setCode(verified ? 200 : 400);
        return result;
    }

    public void processVerification() {
        this.setVerified(true);
        this.setMessage("Verification processed successfully");
        this.setCode(200);
    }
}

