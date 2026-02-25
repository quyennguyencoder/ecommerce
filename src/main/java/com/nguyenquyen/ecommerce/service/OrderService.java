package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dtos.OrderDTO;
import com.nguyenquyen.ecommerce.enums.OrderStatus;
import com.nguyenquyen.ecommerce.mapper.OrderMapper;
import com.nguyenquyen.ecommerce.models.Order;
import com.nguyenquyen.ecommerce.models.User;
import com.nguyenquyen.ecommerce.repository.OrderRepository;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import com.nguyenquyen.ecommerce.service.intf.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDTO.getUserId()));
        Order newOrder = orderMapper.orderDTOToOrder(orderDTO);
        newOrder.setUser(user);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus(OrderStatus.PENDING);

        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now(): orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Shipping date cannot be in the past");
        }
        newOrder.setShippingDate(shippingDate);
        newOrder.setActive(true);

        Order savedOrder = orderRepository.save(newOrder);
        return savedOrder;

    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDTO.getUserId()));
        Order updatedOrder = orderMapper.orderDTOUpdateToOrder(orderDTO, existingOrder);

        return orderRepository.save(updatedOrder);
    }

    @Override
    public void deleteOrderById(Long id) {
        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        existingOrder.setActive(false);
        orderRepository.save(existingOrder);
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
