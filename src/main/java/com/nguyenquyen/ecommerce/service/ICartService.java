package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.AddToCartRequest;
import com.nguyenquyen.ecommerce.dto.request.UpdateCartItemRequest;
import com.nguyenquyen.ecommerce.dto.response.CartResponse;

public interface ICartService {

    CartResponse getMyCart();
    CartResponse addToCart(AddToCartRequest request);
    CartResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request);
    CartResponse removeFromCart(Long cartItemId);
    void clearCart();
}
