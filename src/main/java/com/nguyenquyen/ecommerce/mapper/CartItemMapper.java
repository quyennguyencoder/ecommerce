package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.response.cart.CartItemResponse;
import com.nguyenquyen.ecommerce.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "variantId", source = "variant.id")
    @Mapping(target = "variantSku", source = "variant.sku")
    @Mapping(target = "productName", source = "variant.product.name")
    @Mapping(target = "price", source = "variant.price")
    @Mapping(target = "image", source = "variant.image")
    CartItemResponse cartItemToCartItemResponse(CartItem cartItem);
}
