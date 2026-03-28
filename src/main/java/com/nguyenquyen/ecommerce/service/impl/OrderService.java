package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.order.OrderCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.order.OrderUpdateRequest;
import com.nguyenquyen.ecommerce.dto.request.order.UpdateOrderStatusRequest;
import com.nguyenquyen.ecommerce.dto.response.OrderResponse;
import com.nguyenquyen.ecommerce.exception.AppException;
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
import java.util.Set;
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
    public List<OrderResponse> getAllOrders(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAllOrdersWithStatus(status, pageable);

        return orders.getContent().stream()
                .map(orderMapper::orderToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException("Đơn hàng không tồn tại với id: " + id));

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
                .phone(request.getPhone())
                .email(request.getEmail())
                .shippingAddress(request.getShippingAddress())
                .note(request.getNote())
                .shippingMethod(request.getShippingMethod())
                .carrier(request.getCarrier())
                .shippingFee(request.getShippingFee())
                .total(request.getTotal())
                .status("PENDING")
                .user(currentUser)
                .orderDetails(new HashSet<>())
                .build();

        // Nếu có couponId, lấy và set coupon
        if (request.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new AppException("Coupon không tồn tại với id: " + request.getCouponId()));
            order.setCoupon(coupon);
        }

        // Lưu order trước
        Order savedOrder = orderRepository.save(order);

        // Xử lý cartItemIds để tạo OrderDetail
        if (request.getCartItemIds() != null && !request.getCartItemIds().isEmpty()) {
            for (Long cartItemId : request.getCartItemIds()) {
                CartItem cartItem = cartItemRepository.findById(cartItemId)
                        .orElseThrow(() -> new AppException("Cart item không tồn tại với id: " + cartItemId));

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
    public OrderResponse updateOrder(Long id, OrderUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException("Đơn hàng không tồn tại với id: " + id));

        // Cập nhật các trường nếu có giá trị
        if (request.getName() != null && !request.getName().isEmpty()) {
            order.setName(request.getName());
        }

        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            order.setPhone(request.getPhone());
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            order.setEmail(request.getEmail());
        }

        if (request.getShippingAddress() != null && !request.getShippingAddress().isEmpty()) {
            order.setShippingAddress(request.getShippingAddress());
        }

        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }

        if (request.getShippingMethod() != null && !request.getShippingMethod().isEmpty()) {
            order.setShippingMethod(request.getShippingMethod());
        }

        if (request.getCarrier() != null && !request.getCarrier().isEmpty()) {
            order.setCarrier(request.getCarrier());
        }

        if (request.getShippingFee() != null) {
            order.setShippingFee(request.getShippingFee());
        }

        if (request.getTotal() != null) {
            order.setTotal(request.getTotal());
        }

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            order.setStatus(request.getStatus());
        }

        // Cập nhật coupon nếu có
        if (request.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new AppException("Coupon không tồn tại với id: " + request.getCouponId()));
            order.setCoupon(coupon);
        } else if (request.getCouponId() == null && order.getCoupon() != null) {
            // Xóa coupon nếu couponId là null
            order.setCoupon(null);
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException("Đơn hàng không tồn tại với id: " + id));

        order.setStatus(request.getStatus());
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException("Đơn hàng không tồn tại với id: " + id));

        orderRepository.delete(order);
    }
}
