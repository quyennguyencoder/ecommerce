package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.response.OrderResponse;
import com.nguyenquyen.ecommerce.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class})
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    OrderResponse orderToOrderResponse(Order order);
}
