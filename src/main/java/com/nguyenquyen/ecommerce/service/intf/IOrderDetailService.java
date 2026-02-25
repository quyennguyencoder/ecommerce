package com.nguyenquyen.ecommerce.service.intf;

import com.nguyenquyen.ecommerce.dtos.OrderDetailDTO;
import com.nguyenquyen.ecommerce.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO);
    OrderDetail getOrderDetailById(Long id);
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(Long id);
    List<OrderDetail> getAllOrderDetailsByOrderId(Long orderId);
}
