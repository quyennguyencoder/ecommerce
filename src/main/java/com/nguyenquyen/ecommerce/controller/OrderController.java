package com.nguyenquyen.ecommerce.controller;


import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.PaginationResponse;
import com.nguyenquyen.ecommerce.dto.request.OrderCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.OrderCalculationResponse;
import com.nguyenquyen.ecommerce.dto.response.OrderResponse;
import com.nguyenquyen.ecommerce.enums.OrderStatus;
import com.nguyenquyen.ecommerce.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;


    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderCreateRequest request) {
        OrderResponse order = orderService.createOrder(request);

        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo đơn hàng thành công")
                .data(order)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<OrderResponse>>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var paginationResponse = orderService.getAllOrders(status, page, size);

        ApiResponse<PaginationResponse<OrderResponse>> response = ApiResponse.<PaginationResponse<OrderResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách đơn hàng thành công")
                .data(paginationResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);

        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy đơn hàng thành công")
                .data(order)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<PaginationResponse<OrderResponse>>> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var paginationResponse = orderService.getMyOrders(page, size);

        ApiResponse<PaginationResponse<OrderResponse>> response = ApiResponse.<PaginationResponse<OrderResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách đơn hàng của tôi thành công")
                .data(paginationResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        OrderResponse order = orderService.updateOrderStatus(id, status);

        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật trạng thái đơn hàng thành công")
                .data(order)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa đơn hàng thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate-total")
    public ResponseEntity<ApiResponse<OrderCalculationResponse>> calculateTotalPrice(
            @Valid @RequestBody OrderCreateRequest request
    ){
        OrderCalculationResponse calculation = orderService.calculateTotalPrice(request);

        ApiResponse<OrderCalculationResponse> response = ApiResponse.<OrderCalculationResponse>builder()
                .status(HttpStatus.OK)
                .message("Tính tổng tiền đơn hàng thành công")
                .data(calculation)
                .build();

        return ResponseEntity.ok(response);
    }
}

