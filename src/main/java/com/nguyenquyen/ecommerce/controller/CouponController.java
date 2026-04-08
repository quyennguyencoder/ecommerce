package com.nguyenquyen.ecommerce.controller;


import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.CouponCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.CouponUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.CouponResponse;
import com.nguyenquyen.ecommerce.service.ICouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final ICouponService couponService;

    @PostMapping
    public ResponseEntity<ApiResponse<CouponResponse>> createCoupon(
            @Valid @RequestBody CouponCreateRequest request) {

        CouponResponse coupon = couponService.createCoupon(request);

        ApiResponse<CouponResponse> response = ApiResponse.<CouponResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo coupon thành công")
                .data(coupon)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CouponResponse>>> getAllCoupons() {
        List<CouponResponse> coupons = couponService.getAllCoupons();

        ApiResponse<List<CouponResponse>> response = ApiResponse.<List<CouponResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách coupon thành công")
                .data(coupons)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponResponse>> getCouponById(@PathVariable Long id) {
        CouponResponse coupon = couponService.getCouponById(id);

        ApiResponse<CouponResponse> response = ApiResponse.<CouponResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin coupon thành công")
                .data(coupon)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponResponse>> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody CouponUpdateRequest request) {

        CouponResponse coupon = couponService.updateCoupon(id, request);

        ApiResponse<CouponResponse> response = ApiResponse.<CouponResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật coupon thành công")
                .data(coupon)
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<CouponResponse>> updateCouponActive(
            @PathVariable Long id,
            @RequestParam boolean active) {

        CouponResponse coupon = couponService.updateCouponActive(id, active);

        ApiResponse<CouponResponse> response = ApiResponse.<CouponResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật trạng thái coupon thành công")
                .data(coupon)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa coupon thành công")
                .build();

        return ResponseEntity.ok(response);
    }
}
