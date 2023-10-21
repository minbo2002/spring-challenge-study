package com.example.springchallenge2week.domain.service;

import com.example.springchallenge2week.domain.dto.request.CouponCreateRequestDto;
import com.example.springchallenge2week.domain.dto.response.CouponResponse;
import com.example.springchallenge2week.domain.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CouponService {

    // 쿠폰 생성
    CouponResponse createCoupon(CouponCreateRequestDto request);

    // 쿠폰 리스트
    List<CouponResponse> getCoupons();

    // 쿠폰 리스트 + 페이징 + 검색
    Page<Coupon> getCouponsPage(Pageable pageable, String name, String code, LocalDateTime startDate, LocalDateTime endDate);

    // 쿠폰 리스트 + DTO(페이징) + DTO(검색)
    Page<Coupon> getCouponsPageWithDto(Pageable pageable, String name, String code, LocalDate startDate, LocalDate endDate);
}
