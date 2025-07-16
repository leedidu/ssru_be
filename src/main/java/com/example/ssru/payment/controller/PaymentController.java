package com.example.ssru.payment.controller;

import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.exception.ExceptionResponseDto;
import com.example.ssru.payment.dto.PaymentRequestDto;
import com.example.ssru.payment.dto.PaymentResponseDto;
import com.example.ssru.payment.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/prepare")
    public ResponseEntity postPreparePayment(@RequestBody PaymentRequestDto.prepareCheck prepareCheck){
        boolean check = paymentService.postPreparePayment(prepareCheck);
        if(check){
            PaymentResponseDto.Response response = new PaymentResponseDto.Response("success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ExceptionResponseDto.Response responseBody = new ExceptionResponseDto.Response(
                "fail",
                CustomExceptionCode.PREPARE_PAYMENT_FAIL.getCode(),
                CustomExceptionCode.PREPARE_PAYMENT_FAIL.getMessage()
        );
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/prepare/{merchant_uid}")
    public ResponseEntity checkPreparePayment(@PathVariable("merchant_uid") String uid){
        boolean check = paymentService.checkPreparePayment(uid);
        if(check){
            PaymentResponseDto.Response response = new PaymentResponseDto.Response("success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ExceptionResponseDto.Response responseBody = new ExceptionResponseDto.Response(
                "fail",
                CustomExceptionCode.CHECK_PREPARE_PAYMENT_FAIL.getCode(),
                CustomExceptionCode.CHECK_PREPARE_PAYMENT_FAIL.getMessage()
        );
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/save/{merchant_uid}")
    public ResponseEntity savePayment(@PathVariable("merchant_uid") String uid,
                                      @RequestBody PaymentRequestDto.prepareCheck prepareCheck){
        boolean check = paymentService.checkPaymentAndSave(uid, prepareCheck);
        if(check){
            PaymentResponseDto.Response response = new PaymentResponseDto.Response("success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ExceptionResponseDto.Response responseBody = new ExceptionResponseDto.Response(
                "fail",
                CustomExceptionCode.SAVE_PAYMENT_FAIL.getCode(),
                CustomExceptionCode.SAVE_PAYMENT_FAIL.getMessage()
        );
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
