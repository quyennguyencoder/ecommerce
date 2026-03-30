package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.OrderCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.OrderResponse;
import com.nguyenquyen.ecommerce.enums.OrderStatus;
import com.nguyenquyen.ecommerce.enums.ShippingMethod;
import com.nguyenquyen.ecommerce.mapper.OrderMapper;
import com.nguyenquyen.ecommerce.model.CartItem;
import com.nguyenquyen.ecommerce.model.Coupon;
import com.nguyenquyen.ecommerce.model.Order;
import com.nguyenquyen.ecommerce.model.OrderDetail;
import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.CartItemRepository;
import com.nguyenquyen.ecommerce.repository.CouponRepository;
import com.nguyenquyen.ecommerce.repository.OrderDetailRepository;
import com.nguyenquyen.ecommerce.repository.OrderRepository;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import com.nguyenquyen.ecommerce.service.IOrderService;
import com.nguyenquyen.ecommerce.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAllOrdersWithStatus(status, pageable);

        return orders.getContent().stream()
                .map(orderMapper::orderToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(int page, int size) {
        Long currentUserId = securityUtil.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAllByUserId(currentUserId, pageable);

        return orders.getContent().stream()
                .map(orderMapper::orderToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với id: " + id));

        return orderMapper.orderToOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        // Lấy user hiện tại từ SecurityUtil
        User currentUser = securityUtil.getCurrentUser();

        // Tạo Order mới
        Order order = Order.builder()
                .name(request.getName())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .shippingAddress(request.getShippingAddress())
                .note(request.getNote())
                .shippingMethod(request.getShippingMethod() != null ? request.getShippingMethod() : ShippingMethod.STANDARD)
                .shippingFee(request.getShippingFee())
                .total(request.getTotal())
                .status(OrderStatus.PENDING)
                .user(currentUser)
                .orderDetails(new HashSet<>())
                .build();

        // Nếu có couponCode, lấy và set coupon
        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            Coupon coupon = couponRepository.findByCode(request.getCouponCode())
                    .orElseThrow(() -> new RuntimeException("Coupon không tồn tại với code: " + request.getCouponCode()));
            order.setCoupon(coupon);
        }

        // Lưu order trước
        Order savedOrder = orderRepository.save(order);

        // Xử lý cartItemIds để tạo OrderDetail
        if (request.getCartItemIds() != null && !request.getCartItemIds().isEmpty()) {
            for (Long cartItemId : request.getCartItemIds()) {
                CartItem cartItem = cartItemRepository.findById(cartItemId)
                        .orElseThrow(() -> new RuntimeException("Cart item không tồn tại với id: " + cartItemId));

                // Tạo OrderDetail từ CartItem
                OrderDetail orderDetail = OrderDetail.builder()
                        .order(savedOrder)
                        .variant(cartItem.getVariant())
                        .price(cartItem.getVariant().getPrice())
                        .quantity(cartItem.getQuantity())
                        .build();

                orderDetailRepository.save(orderDetail);
                savedOrder.getOrderDetails().add(orderDetail);
            }

            // Lưu lại order với các order details
            savedOrder = orderRepository.save(savedOrder);
        }

        return orderMapper.orderToOrderResponse(savedOrder);
    }


    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với id: " + id));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với id: " + id));

        orderRepository.delete(order);
    }
}
