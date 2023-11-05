package com.example.springchallenge1week.domain.coupon.service;

import com.example.springchallenge1week.domain.coupon.dto.request.CouponCreateRequestDto;
import com.example.springchallenge1week.domain.coupon.dto.response.CouponResponse;

import java.util.List;

public interface CouponService {

    CouponResponse createCoupon(CouponCreateRequestDto request);

    List<CouponResponse> getCoupons();
}
