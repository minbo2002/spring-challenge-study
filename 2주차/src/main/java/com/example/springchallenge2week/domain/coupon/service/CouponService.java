package com.example.springchallenge2week.domain.coupon.service;

import com.example.springchallenge2week.domain.coupon.dto.request.CouponCreateRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponSearchRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.response.CouponHistoryResponse;
import com.example.springchallenge2week.domain.coupon.dto.response.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CouponService {

    // 쿠폰 생성
    CouponResponse createCoupon(CouponCreateRequestDto request);

    // 쿠폰 발급
    CouponHistoryResponse createCouponHistory(Long userId, String couponCode);

    // 단일 쿠폰 확인
    CouponResponse getCoupon(String couponCode);

    // 쿠폰 리스트
    List<CouponResponse> getCoupons();

    // 쿠폰 리스트 + 페이징 + 검색
    Page<CouponResponse> getCouponsPage(Pageable pageable, String name, String code, String strStartDate, String strEndDate);

    // 쿠폰 리스트 + DTO(페이징) + DTO(검색)
    Page<CouponResponse> getCouponsPageWithDto(Pageable pageable, String name, String code, LocalDate startDateTime, LocalDate endDateTime);

    // 쿠폰 리스트 + Querydsl(페이징, 검색)
    Page<CouponResponse> getCouponsPageWithQueryDsl(CouponSearchRequestDto requestDto);
}
