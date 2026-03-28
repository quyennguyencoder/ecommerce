package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.cart.AddToCartRequest;
import com.nguyenquyen.ecommerce.dto.request.cart.UpdateCartItemRequest;
import com.nguyenquyen.ecommerce.dto.response.CartResponse;
import com.nguyenquyen.ecommerce.service.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getMyCart() {
        CartResponse cart = cartService.getMyCart();

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy giỏ hàng thành công")
                .data(cart)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @Valid @RequestBody AddToCartRequest request) {

        CartResponse cart = cartService.addToCart(request);

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Thêm sản phẩm vào giỏ hàng thành công")
                .data(cart)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        CartResponse cart = cartService.updateCartItem(cartItemId, request);

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật sản phẩm trong giỏ hàng thành công")
                .data(cart)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(
            @PathVariable Long cartItemId) {

        CartResponse cart = cartService.removeFromCart(cartItemId);

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK)
                .message("Xóa sản phẩm khỏi giỏ hàng thành công")
                .data(cart)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        cartService.clearCart();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa giỏ hàng thành công")
                .build();

        return ResponseEntity.ok(response);
    }
}

