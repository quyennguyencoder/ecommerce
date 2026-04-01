package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.OrderCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.OrderResponse;
import com.nguyenquyen.ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    Page<OrderResponse> getAllOrders(OrderStatus status, Pageable pageable);
    Page<OrderResponse> getMyOrders(Pageable pageable);
    OrderResponse getOrderById(Long id);
    OrderResponse createOrder(OrderCreateRequest request);
    OrderResponse updateOrderStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
}
