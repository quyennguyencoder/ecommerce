package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dtos.OrderDTO;
import com.nguyenquyen.ecommerce.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "userId", target = "user.id")
    Order orderDTOToOrder(OrderDTO orderDTO);

    @Mapping(source = "userId", target = "user.id")
    Order orderDTOUpdateToOrder(OrderDTO orderDTO, @MappingTarget Order order);
}

