package com.nguyenquyen.ecommerce.exception;


import lombok.*;

@Getter
@Setter
public class AppException extends RuntimeException {
//    private ErrorCode errorCode;
//
//    public AppException(ErrorCode errorCode) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//    }

    public AppException(String message) {
        super(message);
    }
}