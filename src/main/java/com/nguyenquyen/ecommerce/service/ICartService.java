package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.cart.AddToCartRequest;
import com.nguyenquyen.ecommerce.dto.request.cart.UpdateCartItemRequest;
import com.nguyenquyen.ecommerce.dto.response.cart.CartResponse;

public interface ICartService {

    CartResponse getMyCart();
    CartResponse addToCart(AddToCartRequest request);
    CartResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request);
    CartResponse removeFromCart(Long cartItemId);
    void clearCart();
}
