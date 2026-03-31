package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.CartAddToCartRequest;
import com.nguyenquyen.ecommerce.dto.request.CartItemUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.CartResponse;

public interface ICartService {

    CartResponse getMyCart();
    CartResponse addToCart(CartAddToCartRequest request);
    CartResponse updateCartItem(Long cartItemId, CartItemUpdateRequest request);
    CartResponse removeFromCart(Long cartItemId);
    void clearCart();
}
