package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.request.CouponCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.CouponUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.CouponResponse;
import com.nguyenquyen.ecommerce.model.Coupon;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    CouponResponse toResponse(Coupon coupon);

    Coupon toEntity(CouponCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCouponFromRequest(CouponUpdateRequest request, @MappingTarget Coupon coupon);
}
