package com.example.springchallenge2week.domain.coupon.service;

import com.example.springchallenge2week.domain.coupon.dto.request.CouponCreateRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.request.CouponSearchRequestDto;
import com.example.springchallenge2week.domain.coupon.dto.response.CouponResponse;
import com.example.springchallenge2week.domain.coupon.entity.Coupon;
import com.example.springchallenge2week.domain.coupon.entity.CouponStatus;
import com.example.springchallenge2week.domain.coupon.entity.CouponType;
import com.example.springchallenge2week.domain.coupon.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import java.util.stream.LongStream;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class CouponServiceImplTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponServiceImpl couponService;

    private Coupon coupon1, coupon2;

    @BeforeEach
    void setUp() {

        LongStream.rangeClosed(1,10).forEach(i -> {
            couponRepository.save(
                    Coupon.builder()
                            .name("쿠폰" + i)
                            .code("코드" + i)
                            .type(CouponType.DISCOUNT)
                            .status(CouponStatus.PUBLIC)
                            .startDate(LocalDate.of(2023, 10, 25))
                            .endDate(LocalDate.of(2023, 10, 30))
                            .build()
            );
        });
    }

    @AfterEach
    void after() {
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("쿠폰 리스트 + 페이징 + 검색")
    public void getCouponsPage() {
        // given

        // when
        Page<CouponResponse> couponsPage = couponService.getCouponsPage(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")),
                "쿠폰",
                "코드",
                "2023-10-25",
                "2023-10-30");

        // then
        assertThat(couponsPage.getTotalElements()).isEqualTo(10);  // 페이지 개수 10개인지 확인
        assertThat(couponsPage.getContent().get(0).getName()).isEqualTo("쿠폰10");  // index0의 쿠폰이름이 쿠폰10인지 확인
    }

    @Test
    @DisplayName("쿠폰 리스트 + DTO(페이징) + DTO(검색)")
    public void getCouponsPageWithDto() {
        // given

        // when
        Page<CouponResponse> couponsPage = couponService.getCouponsPageWithDto(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")),
                "쿠폰",
                "코드",
                LocalDate.of(2023, 10, 25),
                LocalDate.of(2023, 10, 30));

        // then
        assertThat(couponsPage.getTotalElements()).isEqualTo(10);  // 페이지 개수 10개인지 확인
        assertThat(couponsPage.getContent().get(5).getId()).isEqualTo(5L);  // index5의 쿠폰아이디가 5인지 확인
        assertThat(couponsPage.getContent().get(4).getName()).isEqualTo("쿠폰6");  // index4의 쿠폰이름이 쿠폰6인지 확인
    }

    @Test
    @DisplayName("쿠폰 리스트 + Querydsl(페이징, 검색)")
    public void getCouponsPageWithQueryDsl() {
        // given

        // when
        Page<CouponResponse> couponsPage = couponService.getCouponsPageWithQueryDsl(
                CouponSearchRequestDto.builder()
                        .name("쿠폰")
                        .code("코드")
                        .startDate(LocalDate.of(2023, 10, 25))
                        .endDate(LocalDate.of(2023, 10, 30))
                        .build()
        );

        // then
        assertThat(couponsPage.getTotalElements()).isEqualTo(10);  // 페이지 개수 10개인지 확인
        assertThat(couponsPage.getContent().get(5).getId()).isEqualTo(5L);  // index3의 쿠폰아이디가 5인지 확인
        assertThat(couponsPage.getContent().get(4).getCode()).isEqualTo("코드6");  // index4의 쿠폰코드가 코드6인지 확인
    }

    @Test
    @DisplayName("쿠폰 생성 테스트")
    public void createCoupon() {
        // given
        CouponCreateRequestDto requestDto = CouponCreateRequestDto.builder()
                .name("쿠폰11")
                .code("코드11")
                .type(CouponType.DISCOUNT)
                .status(CouponStatus.PUBLIC)
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 30))
                .build();

        // when
        CouponResponse couponResponse = couponService.createCoupon(requestDto);

        // then
        assertThat(couponResponse.getId()).isEqualTo(11L);         // 쿠폰 아이디가 11인지 확인
        assertThat(couponResponse.getName()).isEqualTo("쿠폰11");
        assertThat(couponResponse.getCode()).isEqualTo("코드11");
    }

}