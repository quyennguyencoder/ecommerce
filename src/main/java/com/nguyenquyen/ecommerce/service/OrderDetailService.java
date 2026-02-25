package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dtos.OrderDetailDTO;
import com.nguyenquyen.ecommerce.mapper.OrderDetailMapper;
import com.nguyenquyen.ecommerce.models.Order;
import com.nguyenquyen.ecommerce.models.OrderDetail;
import com.nguyenquyen.ecommerce.models.Product;
import com.nguyenquyen.ecommerce.repository.OrderDetailRepository;
import com.nguyenquyen.ecommerce.repository.OrderRepository;
import com.nguyenquyen.ecommerce.repository.ProductRepository;
import com.nguyenquyen.ecommerce.service.intf.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailMapper orderDetailMapper;


    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderDetailDTO.getProductId()));
        OrderDetail newOrderDetail = orderDetailMapper.orderDetailDTOToOrderDetail(orderDetailDTO);
        return orderDetailRepository.save(newOrderDetail);
    }

    @Override
    public OrderDetail getOrderDetailById(Long id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderDetailDTO.getProductId()));
        OrderDetail updatedOrderDetail = orderDetailMapper.updateOrderDetailFromDTO(orderDetailDTO, existingOrderDetail);
        return orderDetailRepository.save(updatedOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> getAllOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
