package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.order.OrderCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.order.OrderUpdateRequest;
import com.nguyenquyen.ecommerce.dto.request.order.UpdateOrderStatusRequest;
import com.nguyenquyen.ecommerce.dto.response.OrderResponse;

import java.util.List;

public interface IOrderService {
    List<OrderResponse> getAllOrders(String status, int page, int size);
    OrderResponse getOrderById(Long id);
    OrderResponse createOrder(OrderCreateRequest request);
    OrderResponse updateOrder(Long id, OrderUpdateRequest request);
    OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request);
    void deleteOrder(Long id);
}
