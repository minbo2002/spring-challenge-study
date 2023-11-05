package com.example.springchallenge2week.domain.coupon.service;

import com.example.springchallenge2week.common.exception.CustomApiException;
import com.example.springchallenge2week.common.exception.ResponseCode;
import com.example.springchallenge2week.common.utils.aws.AwsS3Util;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponCreateRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponSearchRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponWithImageCreateRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.response.CouponHistoryResponse;
import com.example.springchallenge2week.domain.coupon.dto.response.CouponResponse;
import com.example.springchallenge2week.domain.coupon.entity.*;
import com.example.springchallenge2week.domain.coupon.repository.CouponHistoryRepository;
import com.example.springchallenge2week.domain.coupon.repository.CouponRepository;
import com.example.springchallenge2week.domain.user.entity.User;
import com.example.springchallenge2week.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

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

    private final CouponHistoryRepository couponHistoryRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final AwsS3Util awsS3Util;

    // 유효날짜 지난 쿠폰히스토리 상태 변경
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void run() {

        List<Coupon> couponList = couponRepository.findByEndAtBefore(LocalDate.now());

        // 조회한 쿠폰들의 PK를 추출
        List<Long> couponIds = couponList.stream()
                .map(Coupon::getId)
                .collect(Collectors.toList());

        for(Long couponId : couponIds) {

            List<CouponHistory> couponHistories = couponHistoryRepository.findByCouponId(couponId);

            for(CouponHistory couponHistory : couponHistories) {
                couponHistory.updateStatus(couponHistory.getStatus());
            }
        }
    }

    // 쿠폰 생성
    @Transactional
    @Override
    public CouponResponse createCoupon(CouponCreateRequestDto request) {

        Coupon saveCoupon = couponRepository.save(request.toEntity());

        return CouponResponse.toDto(saveCoupon);
    }

    // 쿠폰 생성(이미지)
    @Transactional
    @Override
    public void createCouponWithImage(CouponWithImageCreateRequestDto request) {

        Coupon coupon = request.toEntity();

        MultipartFile logoImage = request.getLogoImage();
        String uploadedUrl = null;

        if (!ObjectUtils.isEmpty(logoImage)) {
            log.info("logoImage is not empty, logoImage: {}", logoImage);
            uploadedUrl = awsS3Util.uploadFile(logoImage);
            coupon.changeLogoImage(uploadedUrl);
        }

        Coupon saveCoupon = couponRepository.save(coupon);
    }

    // 쿠폰 발급 (쿠폰 히스토리 생성)
    @Transactional
    @Override
    public CouponHistoryResponse createCouponHistory(Long userId, String couponCode) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(ResponseCode.NO_TARGET));
        log.info("ServiceImpl user: {}", user);

        Coupon coupon = couponRepository.findByCouponCode(couponCode);
        log.info("ServiceImpl coupon: {}", coupon);

        if(coupon == null) {
            throw new CustomApiException(ResponseCode.NO_TARGET_COUPON);
        }else if(coupon.getType() != CouponType.DISCOUNT){
            throw new CustomApiException(ResponseCode.INVALID_DISCOUNT);
        }else if(coupon.getStatus() == CouponStatus.PRIVATE) {
            throw new CustomApiException(ResponseCode.PRIVATE_COUPON);
        }else if(couponHistoryRepository.findByUserAndCoupon(user.getId(), coupon.getId()) != null) {
            throw new CustomApiException(ResponseCode.ALREADY_ISSUED_COUPON);
        }

        CouponHistory couponHistory = CouponHistory.builder()
                .status(CouponHistoryStatus.UNUSED)
                .type(CouponHistoryType.NO)
                .usedAt(null)
                .issuedAt(LocalDateTime.now())
                .user(user)
                .coupon(coupon)
                .build();

        CouponHistory saveCouponHistory = couponHistoryRepository.save(couponHistory);

        return CouponHistoryResponse.toDto(saveCouponHistory);
    }

    // 단일 쿠폰 확인
    @Override
    public CouponResponse getCoupon(String couponCode) {

        Coupon coupon = couponRepository.findByCouponCode(couponCode);

        return CouponResponse.toDto(coupon);
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

        Page<Coupon> couponPage = couponRepository.findCouponWithQueryDsl(requestDto);

        // Coupon 엔티티를 CouponResponse로 변환
        Page<CouponResponse> couponResponsePage = couponPage
                .map(coupon -> {String statusDescription = getStatusDescription(coupon);

            return CouponResponse.toDto(coupon, statusDescription);
        });

        return couponResponsePage;
    }

    private String getStatusDescription(Coupon coupon) {
        List<CouponHistory> histories = coupon.getCouponHistoryies();

        for (CouponHistory history : histories) {
            if (history.getStatus() == CouponHistoryStatus.EXPIRED) {
                return "만료";
            }
        }

        return "활성화";
    }
}
