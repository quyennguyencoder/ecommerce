package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.response.cart.CartResponse;
import com.nguyenquyen.ecommerce.model.Cart;
import com.nguyenquyen.ecommerce.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Set;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "totalItems", expression = "java(cart.getCartItems() == null ? 0 : cart.getCartItems().size())")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(cart.getCartItems()))")
    CartResponse cartToCartResponse(Cart cart);

    default BigDecimal calculateTotalPrice(Set<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return cartItems.stream()
                .map(item -> item.getVariant().getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}








