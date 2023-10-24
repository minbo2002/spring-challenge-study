package com.example.springchallenge2week.domain.repository;

import com.example.springchallenge2week.domain.dto.request.CouponSearchRequestDto;
import com.example.springchallenge2week.domain.entity.Coupon;
import org.springframework.data.domain.Page;

public interface CouponRepositoryCustom {

    Page<Coupon> findCouponWithQueryDsl(CouponSearchRequestDto requestDto);
}
