package com.sparta.bapzip.payment.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class PaymentException extends GlobalException {

    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }
}