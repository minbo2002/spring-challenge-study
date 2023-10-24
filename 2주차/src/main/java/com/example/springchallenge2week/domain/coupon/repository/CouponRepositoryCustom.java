package com.example.springchallenge2week.domain.coupon.repository;

import com.example.springchallenge2week.domain.coupon.dto.request.CouponSearchRequestDto;
import com.example.springchallenge2week.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;

public interface CouponRepositoryCustom {

    Page<Coupon> findCouponWithQueryDsl(CouponSearchRequestDto requestDto);
}
