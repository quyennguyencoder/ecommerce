package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.config.RabbitMQConfig;
import com.nguyenquyen.ecommerce.dto.OrderEmailEvent;
import com.nguyenquyen.ecommerce.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService implements INotificationService {
    private final JavaMailSender javaMailSender;


    @Override
    @RabbitListener(queues = RabbitMQConfig.QUEUE_EMAIL)
    public void sendEmail(OrderEmailEvent event) throws IOException {
        System.out.println("Nhận được event gửi email cho đơn hàng: " + event.getOrderId());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("quyen0012341982@gmail.com"); // Cùng email đã cấu hình
            message.setTo(event.getCustomerEmail());
            message.setSubject("Xác nhận đơn hàng: " + event.getOrderId());

            String body = "Chào " + event.getCustomerName() + ",\n\n" +
                    "Đơn hàng " + event.getOrderId() + " của bạn đã được đặt thành công.\n" +
                    "Tổng tiền: " + event.getTotalAmount() + " VNĐ.\n\n" +
                    "Cảm ơn bạn đã mua sắm!";

            message.setText(body);

            javaMailSender.send(message);
            System.out.println("Đã gửi email thành công tới: " + event.getCustomerEmail());

        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
            // Tùy theo business logic, bạn có thể implement cơ chế retry tại đây (như đẩy vào Dead Letter Queue)
        }
    }
}
