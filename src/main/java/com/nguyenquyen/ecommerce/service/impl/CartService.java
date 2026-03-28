package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.cart.AddToCartRequest;
import com.nguyenquyen.ecommerce.dto.request.cart.UpdateCartItemRequest;
import com.nguyenquyen.ecommerce.dto.response.CartResponse;
import com.nguyenquyen.ecommerce.mapper.CartMapper;
import com.nguyenquyen.ecommerce.model.Cart;
import com.nguyenquyen.ecommerce.model.CartItem;
import com.nguyenquyen.ecommerce.model.ProductVariant;
import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.CartItemRepository;
import com.nguyenquyen.ecommerce.repository.CartRepository;
import com.nguyenquyen.ecommerce.repository.ProductVariantRepository;
import com.nguyenquyen.ecommerce.service.ICartService;
import com.nguyenquyen.ecommerce.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartMapper cartMapper;
    private final SecurityUtil securityUtil;

    /**
     * Lấy cart của user hiện tại, tạo nếu chưa có
     */
    private Cart getCurrentUserCart() {
        User currentUser = securityUtil.getCurrentUser();
        Cart cart = currentUser.getCart();
        if (cart == null) {
            cart = Cart.builder()
                    .user(currentUser)
                    .build();
            cart = cartRepository.save(cart);
            currentUser.setCart(cart);
        }
        return cart;
    }

    @Override
    public CartResponse getMyCart() {
        Cart currentUserCart = getCurrentUserCart();
        return cartMapper.cartToCartResponse(currentUserCart);
    }

    @Override
    public CartResponse addToCart(AddToCartRequest request) {
        // Lấy cart của user hiện tại
        Cart cart = getCurrentUserCart();

        // Kiểm tra xem variant có tồn tại không
        ProductVariant variant = productVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + request.getVariantId()));

        // Kiểm tra xem sản phẩm này đã có trong giỏ hàng chưa
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getVariant().getId().equals(request.getVariantId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Nếu đã có, cộng thêm số lượng
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            // Nếu chưa có, tạo mới
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .variant(variant)
                    .quantity(request.getQuantity())
                    .build();
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        // Lưu lại cart
        cart = cartRepository.save(cart);
        return cartMapper.cartToCartResponse(cart);
    }

    @Override
    public CartResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request) {
        // Lấy cart item từ database
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng với ID: " + cartItemId));

        // Kiểm tra xem cartItem có thuộc về user hiện tại không
        Cart currentUserCart = getCurrentUserCart();
        if (!cartItem.getCart().getId().equals(currentUserCart.getId())) {
            throw new RuntimeException("Sản phẩm không thuộc giỏ hàng của bạn");
        }

        // Cập nhật số lượng
        if (request.getQuantity() != null && request.getQuantity() > 0) {
            cartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        // Trả về giỏ hàng cập nhật
        Cart updatedCart = cartRepository.findById(currentUserCart.getId())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));
        return cartMapper.cartToCartResponse(updatedCart);
    }

    @Override
    public CartResponse removeFromCart(Long cartItemId) {
        // Lấy cart item từ database
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng với ID: " + cartItemId));

        // Kiểm tra xem cartItem có thuộc về user hiện tại không
        Cart currentUserCart = getCurrentUserCart();
        if (!cartItem.getCart().getId().equals(currentUserCart.getId())) {
            throw new RuntimeException("Sản phẩm không thuộc giỏ hàng của bạn");
        }

        // Xóa item khỏi giỏ hàng
        currentUserCart.getCartItems().remove(cartItem);
        cartItemRepository.deleteById(cartItemId);

        // Trả về giỏ hàng cập nhật
        Cart updatedCart = cartRepository.findById(currentUserCart.getId())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));
        return cartMapper.cartToCartResponse(updatedCart);
    }

    @Override
    public void clearCart() {
        Cart currentUserCart = getCurrentUserCart();
        // Xóa tất cả items khỏi giỏ hàng
        currentUserCart.getCartItems().clear();
        cartRepository.save(currentUserCart);
    }
}
