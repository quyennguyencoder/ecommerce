package com.nguyenquyen.ecommerce.controller;


import com.nguyenquyen.ecommerce.dtos.OrderDTO;
import com.nguyenquyen.ecommerce.dtos.OrderDetailDTO;
import com.nguyenquyen.ecommerce.dtos.response.OrderDetailResponse;
import com.nguyenquyen.ecommerce.mapper.OrderDetailMapper;
import com.nguyenquyen.ecommerce.models.OrderDetail;
import com.nguyenquyen.ecommerce.service.OrderDetailService;
import com.nguyenquyen.ecommerce.service.OrderService;
import com.nguyenquyen.ecommerce.service.intf.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    private final OrderDetailMapper orderDetailMapper;

    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ){
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);

            return ResponseEntity.ok().body(orderDetailMapper.orderDetailToOrderDetailResponse(newOrderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long id) {
        try{
            OrderDetail existingOrderDetail = orderDetailService.getOrderDetailById(id);
            return ResponseEntity.ok().body(orderDetailMapper.orderDetailToOrderDetailResponse(existingOrderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@PathVariable Long orderId) {
        try{
            List<OrderDetail> existingOrderDetail = orderDetailService.getAllOrderDetailsByOrderId(orderId);
            List<OrderDetailResponse> orderDetailResponses = existingOrderDetail.stream()
                    .map(orderDetail -> orderDetailMapper.orderDetailToOrderDetailResponse(orderDetail))
                    .toList();
            return ResponseEntity.ok().body(orderDetailResponses);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @PathVariable Long id,
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result)
    {
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderDetail updatedOrderDetail  =  orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok().body(orderDetailMapper.orderDetailToOrderDetailResponse(updatedOrderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try{
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok().body("Order detail with id: " + id + " deleted successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
