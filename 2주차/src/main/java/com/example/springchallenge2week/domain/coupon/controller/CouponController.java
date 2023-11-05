package com.example.springchallenge2week.domain.coupon.controller;

import com.example.springchallenge2week.common.security.dto.PrincipalDetails;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponCreateRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponDownloadRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponSearchRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponWithImageCreateRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.response.CouponHistoryResponse;
import com.example.springchallenge2week.domain.coupon.dto.response.CouponResponse;
import com.example.springchallenge2week.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 생성
    @PostMapping("/coupon")
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponCreateRequestDto request) {

        CouponResponse coupon = couponService.createCoupon(request);

        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    // 쿠폰 생성(+이미지)
    @PostMapping
    public ResponseEntity<?> createCouponWithImage(
            @Valid @RequestPart(value = "values") CouponWithImageCreateRequestDto request,
            @RequestPart(value = "logoImage", required = false) MultipartFile logoImage,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        log.info("request: {}", request);
        log.info("principalDetails: {}", principalDetails);

        request.setLogoImage(logoImage);
//        request.setDetailImages(detailImages);
//        request.createTag(request);
        couponService.createCouponWithImage(request);

        return new ResponseEntity<>("create coupon with image!", HttpStatus.CREATED);
    }

    // 쿠폰 발급
    @PostMapping("/coupon/user/{userId}/download")
    public ResponseEntity<CouponHistoryResponse> downloadCoupon(@Valid @RequestBody CouponDownloadRequestDto request,
                                                                @PathVariable Long userId) {

        log.info("request: {}", request);
        log.info("userId: {}", userId);

        CouponHistoryResponse couponHistory = couponService.createCouponHistory(userId, request.getCouponCode());

        return ResponseEntity.ok(couponHistory);
    }

    // 단일 쿠폰확인
    @GetMapping("/coupon/{couponCode}")
    public ResponseEntity<CouponResponse> getCoupon(@PathVariable String couponCode) {

        log.info("couponCode: {}", couponCode);
        CouponResponse coupon = couponService.getCoupon(couponCode);

        return ResponseEntity.ok(coupon);
    }

    // 쿠폰 리스트
    @GetMapping("/coupon")
    public ResponseEntity<List<CouponResponse>> getCoupons() {

        List<CouponResponse> coupons = couponService.getCoupons();

        return ResponseEntity.ok(coupons);
    }

    // 쿠폰 리스트 + 페이징 + 검색
    @GetMapping("/coupon/paging")
    public ResponseEntity<Page<CouponResponse>> getCouponsPage(
                @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
                @RequestParam(required = false) String name,
                @RequestParam(required = false) String code,
                @RequestParam(value = "startDate", required = false) String strStartDate,
                @RequestParam(value = "endDate", required = false) String strEndDate) {

        log.info("name: {}, code: {}, strStartDate: {}, strEndDate: {}", name, code, strStartDate, strEndDate);

        Page<CouponResponse> coupons = couponService.getCouponsPage(pageable, name, code, strStartDate, strEndDate);

        return ResponseEntity.ok(coupons);
    }

    // 쿠폰 리스트 + DTO(페이징) + DTO(검색)
    @GetMapping("/coupon/dto")
    public ResponseEntity<Page<CouponResponse>> getCouponsPageWithDto(CouponSearchRequestDto requestDto){

        Pageable pageable = requestDto.getPageable();
        String name = requestDto.getName();
        String code = requestDto.getCode();
        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();

        Page<CouponResponse> coupons = couponService.getCouponsPageWithDto(pageable, name, code, startDate, endDate);

        return ResponseEntity.ok(coupons);
    }

    // 쿠폰 리스트 + QueryDSL(페이징+검색)
    @GetMapping("/coupon/querydsl")
    public ResponseEntity<Page<CouponResponse>> getCouponsPageWithQueryDsl(CouponSearchRequestDto requestDto){

        Page<CouponResponse> coupons = couponService.getCouponsPageWithQueryDsl(requestDto);

        return ResponseEntity.ok(coupons);
    }
}
