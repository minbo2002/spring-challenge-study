package com.example.springchallenge2week.domain.service;

import com.example.springchallenge2week.domain.dto.request.CouponCreateRequestDto;
import com.example.springchallenge2week.domain.dto.response.CouponResponse;
import com.example.springchallenge2week.domain.entity.Coupon;
import com.example.springchallenge2week.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    // 쿠폰 생성
    @Transactional
    @Override
    public CouponResponse createCoupon(CouponCreateRequestDto request) {

        Coupon saveCoupon = couponRepository.save(request.toEntity());

        return CouponResponse.toDto(saveCoupon);
    }

    // 쿠폰 리스트
    @Override
    public List<CouponResponse> getCoupons() {

        return couponRepository.findAll().stream()
                .map(CouponResponse::toDto)
                .collect(Collectors.toList());
    }

    // 쿠폰 리스트 + 페이징 + 검색
    @Override
    public Page<Coupon> getCouponsPage(Pageable pageable, String name, String code, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        return couponRepository.findCouponWithParam(pageable, name, code, startDateTime, endDateTime);
    }

    // 쿠폰 리스트 + DTO(페이징) + DTO(검색)
    @Override
    public Page<Coupon> getCouponsPageWithDto(Pageable pageable, String name, String code, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        return couponRepository.findCouponWithDto(pageable, name, code, startDateTime, endDateTime);
    }
}
