package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.OrderEmailEvent;

import java.io.IOException;

public interface INotificationService {
    void sendEmail(OrderEmailEvent orderEmailEvent) throws IOException;
}
