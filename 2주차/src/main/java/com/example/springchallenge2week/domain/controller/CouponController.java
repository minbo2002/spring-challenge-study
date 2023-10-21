package com.example.springchallenge2week.domain.controller;

import com.example.springchallenge2week.domain.dto.request.CouponCreateRequestDto;
import com.example.springchallenge2week.domain.dto.request.CouponSearchRequestDto;
import com.example.springchallenge2week.domain.dto.response.CouponResponse;
import com.example.springchallenge2week.domain.entity.Coupon;
import com.example.springchallenge2week.domain.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 생성
    @PostMapping("/coupon")
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponCreateRequestDto request) {

        CouponResponse coupon = couponService.createCoupon(request);

        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    // 쿠폰 리스트
    @GetMapping("/coupon")
    public ResponseEntity<List<CouponResponse>> getCoupons() {

        List<CouponResponse> coupons = couponService.getCoupons();

        return ResponseEntity.ok(coupons);
    }

    // 쿠폰 리스트 + 페이징 + 검색
    @GetMapping("/coupon/paging")
    public ResponseEntity<Page<Coupon>> getCouponsPage(
                @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
                @RequestParam(required = false) String name,
                @RequestParam(required = false) String code,
                @RequestParam(value = "startDate", required = false) String strStartDate,
                @RequestParam(value = "endDate", required = false) String strEndDate) {

        log.info("name: {}, code: {}, startDate: {}, endDate: {}", name, code, strStartDate, strEndDate);

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (strStartDate != null && !strStartDate.isEmpty()) {
            // "yyyy-MM-dd" 형식의 패턴으로 변경
            startDate = LocalDate.parse(strStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        }
        if (strEndDate != null && !strEndDate.isEmpty()) {
            // "yyyy-MM-dd" 형식의 패턴으로 변경
            endDate = LocalDate.parse(strEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX);
        }

        log.info("name: {}, code: {}, startDate: {}, endDate: {}", name, code, startDate, endDate);
        Page<Coupon> coupons = couponService.getCouponsPage(pageable, name, code, startDate, endDate);

        return ResponseEntity.ok(coupons);
    }

    // 쿠폰 리스트 + DTO(페이징) + DTO(검색)
    @GetMapping("/coupon/dto")
    public ResponseEntity<Page<Coupon>> getCouponsPageWithDto(CouponSearchRequestDto requestDto){

        Pageable pageable = requestDto.getPageable();
        String name = requestDto.getName();
        String code = requestDto.getCode();
        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();

        Page<Coupon> coupons = couponService.getCouponsPageWithDto(pageable, name, code, startDate, endDate);

        return ResponseEntity.ok(coupons);
    }

    // 쿠폰 리스트 + QueryDSL(페이징+검색)
}
