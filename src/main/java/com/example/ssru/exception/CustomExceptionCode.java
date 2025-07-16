package com.example.ssru.exception;

import lombok.Getter;

public enum CustomExceptionCode {
    PASSWORD_ERROR(100, "비밀번호는 대소문자, 숫자, 특수문자를 포함하여 8-20자여야합니다."),
    SIGN_IN_FAIL(404, "회원가입 실패"),
    TOKEN_IS_NOT_COINCIDE(404, "토큰이 아직 유효합니다."),
    EDIT_INFO_FAIL(404, "회원정보 수정 실패"),
    ADDRESS_NOT_FOUND(404, "해당 주소를 찾을 수 없음"),
    POINT_SHORTAGE(404, "포인트가 부족합니다."),
    SSTEAL_FAIL(404, "쓰틸 신청 실패\n사유 : 쓰틸러가 아니거나 해당 신청내역을 찾을 수 없음"),
    COMPLETE_SSTEAL_FAIL(404, "쓰틸 완료 실패\n사유 : 로그인한 회원과 쓰틸러가 동일하지 않음"),
    CANCEL_SSTEAL_FAIL(404, "쓰틸 취소 실패\n사유를 입력해주세요"),
    APPLICATION_SSTEAL_FAIL(404, "신청 취소 실패\n사유 : 신청내역을 찾을 수 없음"),
    SMS_FAIL(404, "문자 전송 실패"),
    PREPARE_PAYMENT_FAIL(404, "이미 등록된 사전 검증 내역"),
    CHECK_PREPARE_PAYMENT_FAIL(404, "검증 실패"),
    SAVE_PAYMENT_FAIL(404, "포인트 저장 실패"),
    UNABLE_TO_SEND_EMAIL(404, "메일을 보낼 수 없음"),
    EMAIL_VERIFY_FAIL(404, "이메일 인증 실패"),
    ACCOUNT_REGISTER_FAIL(404, "계좌 등록 실패"),
    BILL_PW_IS_NULL(404, "결제 비밀번호가 없음"),
    REFRESH_TOKEN_IS_NOT_VALID(404, "refresh 토큰이 유효하지 않음"),
    LOGIN_ERROR(101, "아이디 또는 비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),
    POINT_HISTORY_NOT_FOUND(404, "포인트 내역을 찾을 수 없습니다."),
    SSTEAL_HISTORY_NOT_FOUNT(404, "쓰틸 내역을 찾을 수 없습니다.");

    @Getter
    public int code;

    @Getter
    public String message;

    CustomExceptionCode(int code, String message){
        this.code = code;
        this.message = message;
    }
}
