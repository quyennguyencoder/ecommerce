package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.CouponCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.CouponUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.CouponResponse;
import com.nguyenquyen.ecommerce.model.Coupon;
import com.nguyenquyen.ecommerce.repository.CouponRepository;
import com.nguyenquyen.ecommerce.service.ICouponService;
import com.nguyenquyen.ecommerce.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService implements ICouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll()
                .stream()
                .map(couponMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CouponResponse getCouponById(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon không tồn tại với id: " + id));
        return couponMapper.toResponse(coupon);
    }

    @Override
    public CouponResponse createCoupon(CouponCreateRequest request) {
        if (couponRepository.findByCode(request.getCode()).isPresent()) {
            throw new RuntimeException("Mã coupon đã tồn tại: " + request.getCode());
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu");
        }

        Coupon coupon = couponMapper.toEntity(request);
        coupon.setUsedCount(0);

        Coupon savedCoupon = couponRepository.save(coupon);
        return couponMapper.toResponse(savedCoupon);
    }

    @Override
    public CouponResponse updateCoupon(Long id, CouponUpdateRequest request) {
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon không tồn tại với id: " + id));

        if (request.getCode() != null && !request.getCode().isBlank()) {
            couponRepository.findByCode(request.getCode())
                    .filter(coupon -> !coupon.getId().equals(id))
                    .ifPresent(coupon -> {
                        throw new RuntimeException("Mã coupon đã tồn tại: " + request.getCode());
                    });
        }

        couponMapper.updateCouponFromRequest(request, existingCoupon);

        validateDateRange(existingCoupon.getStartDate(), existingCoupon.getEndDate());

        Coupon updatedCoupon = couponRepository.save(existingCoupon);
        return couponMapper.toResponse(updatedCoupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new RuntimeException("Coupon không tồn tại với id: " + id);
        }
        couponRepository.deleteById(id);
    }

    @Override
    public CouponResponse updateCouponActive(Long id, boolean active) {
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon không tồn tại với id: " + id));

        existingCoupon.setActive(active);

        Coupon updatedCoupon = couponRepository.save(existingCoupon);
        return couponMapper.toResponse(updatedCoupon);
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu");
        }
    }
}
