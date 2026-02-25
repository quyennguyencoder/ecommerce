package com.nguyenquyen.ecommerce.controller;


import com.nguyenquyen.ecommerce.dtos.OrderDTO;
import com.nguyenquyen.ecommerce.models.Order;
import com.nguyenquyen.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
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

            return ResponseEntity.ok().body(orderService.createOrder(orderDTO));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try{
            Order existingOrder = orderService.getOrderById(id);
            return ResponseEntity.ok().body(existingOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllOrderByUserId(@PathVariable Long userId) {
        try{
            List<Order> orders = orderService.getAllOrdersByUserId(userId);
            return ResponseEntity.ok().body(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO,
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
            Order updatedOrder = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok().body(updatedOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try{
            orderService.deleteOrderById(id);
            return ResponseEntity.ok().body("Order with id: " + id + " deleted successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
