package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.response.OrderDetailResponse;
import com.nguyenquyen.ecommerce.model.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "variantId", source = "variant.id")
    @Mapping(target = "variantSku", source = "variant.sku")
    @Mapping(target = "productName", source = "variant.product.name")
    OrderDetailResponse orderDetailToOrderDetailResponse(OrderDetail orderDetail);
}
