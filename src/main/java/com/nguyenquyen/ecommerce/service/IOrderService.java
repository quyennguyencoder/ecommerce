package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.OrderCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.OrderResponse;
import com.nguyenquyen.ecommerce.enums.OrderStatus;

import java.util.List;

public interface IOrderService {
    List<OrderResponse> getAllOrders(OrderStatus status, int page, int size);
    List<OrderResponse> getMyOrders(int page, int size);
    OrderResponse getOrderById(Long id);
    OrderResponse createOrder(OrderCreateRequest request);
    OrderResponse updateOrderStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
}
