package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.PaginationResponse;
import com.nguyenquyen.ecommerce.dto.request.OrderCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.OrderResponse;
import com.nguyenquyen.ecommerce.enums.OrderStatus;

public interface IOrderService {
    PaginationResponse<OrderResponse> getAllOrders(OrderStatus status, int page, int size);
    PaginationResponse<OrderResponse> getMyOrders(int page, int size);
    OrderResponse getOrderById(Long id);
    OrderResponse createOrder(OrderCreateRequest request);
    OrderResponse updateOrderStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
}
