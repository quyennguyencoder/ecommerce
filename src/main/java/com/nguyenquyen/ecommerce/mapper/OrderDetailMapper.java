package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dtos.OrderDetailDTO;
import com.nguyenquyen.ecommerce.dtos.response.OrderDetailResponse;
import com.nguyenquyen.ecommerce.models.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "productId", target = "product.id")
    OrderDetail orderDetailDTOToOrderDetail(OrderDetailDTO orderDetailDTO);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "productId", target = "product.id")
    OrderDetail updateOrderDetailFromDTO(OrderDetailDTO orderDetailDTO, @MappingTarget OrderDetail orderDetail);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    OrderDetailResponse orderDetailToOrderDetailResponse(OrderDetail orderDetail);
}
