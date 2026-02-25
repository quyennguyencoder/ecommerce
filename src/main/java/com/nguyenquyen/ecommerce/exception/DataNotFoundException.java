package com.nguyenquyen.ecommerce.exception;

public class DataNotFoundException extends  RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
