package com.example.springchallenge2week.domain.coupon.service;

import com.example.springchallenge2week.domain.coupon.dto.request.CouponCreateRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponSearchRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.response.CouponResponse;
import com.example.springchallenge2week.domain.coupon.entity.Coupon;
import com.example.springchallenge2week.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    public Page<CouponResponse> getCouponsPage(Pageable pageable, String name, String code, String strStartDate, String strEndDate) {

        LocalDateTime startDateTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

        if (strStartDate != null && !strStartDate.isEmpty()) {
            // "yyyy-MM-dd'T'HH:mm:ss" 패턴으로 변경
            startDateTime = LocalDate.parse(strStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();  // 하루의 시작시간
        }
        if (strEndDate != null && !strEndDate.isEmpty()) {
            // "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS" 패턴으로 변경
            endDateTime = LocalDate.parse(strEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX); // 하루의 마지막시간
        }

        log.info("name: {}, code: {}, startDateTime: {}, endDateTime: {}", name, code, startDateTime, endDateTime);

        return couponRepository.findCouponWithParam(pageable, name, code, startDateTime, endDateTime)
                .map(CouponResponse::toDto);
    }

    // 쿠폰 리스트 + DTO(페이징) + DTO(검색)
    @Override
    public Page<CouponResponse> getCouponsPageWithDto(Pageable pageable, String name, String code, LocalDate startDate, LocalDate endDate) {

        LocalDateTime startDateTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

        if (startDate != null) {
            startDateTime = startDate.atStartOfDay();  // 하루의 시작시간
        }
        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX);;  // 하루의 마지막시간
        }

        log.info("name: {}, code: {}, startDateTime: {}, endDateTime: {}", name, code, startDateTime, endDateTime);

        return couponRepository.findCouponWithDto(pageable, name, code, startDateTime, endDateTime)
                .map(CouponResponse::toDto);
    }

    // 쿠폰 리스트 + Querydsl(페이징, 검색)
    @Override
    public Page<CouponResponse> getCouponsPageWithQueryDsl(CouponSearchRequestDto requestDto) {

        return couponRepository.findCouponWithQueryDsl(requestDto)
                .map(CouponResponse::toDto);
    }
}
