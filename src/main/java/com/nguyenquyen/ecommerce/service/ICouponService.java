package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.CouponCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.CouponUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.CouponResponse;

import java.util.List;

public interface ICouponService {
    List<CouponResponse> getAllCoupons();
    CouponResponse getCouponById(Long id);
    CouponResponse createCoupon(CouponCreateRequest request);
    CouponResponse updateCoupon(Long id, CouponUpdateRequest request);
    void deleteCoupon(Long id);
    CouponResponse updateCouponActive(Long id, boolean active);
}
