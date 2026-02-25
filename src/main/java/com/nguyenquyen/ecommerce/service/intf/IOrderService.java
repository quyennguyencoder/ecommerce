package com.nguyenquyen.ecommerce.service.intf;

import com.nguyenquyen.ecommerce.dtos.OrderDTO;
import com.nguyenquyen.ecommerce.models.Order;


import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO);
    Order getOrderById(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO);
    void deleteOrderById(Long id);
    List<Order> getAllOrdersByUserId(Long userId);
}
