package com.example.springchallenge2week.domain.coupon.repository;

import com.example.springchallenge2week.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long>,
                                          CouponRepositoryCustom{

    // 쿠폰 리스트 + 페이징 + 검색
    @Query("select c from Coupon c where c.name like %:name% and c.code like %:code% and c.createdAt BETWEEN :startDate AND :endDate order by c.id desc")
    Page<Coupon> findCouponWithParam(
            Pageable pageable,
            @Param("name") String name,
            @Param("code") String code,
            @Param("startDate") LocalDateTime startDateTime,
            @Param("endDate") LocalDateTime endDateTime);

    // 쿠폰 리스트 + DTO(페이징) + DTO(검색)
    @Query("select c from Coupon c where c.name like %:name% and c.code like %:code% and c.createdAt BETWEEN :startDate AND :endDate order by c.id desc")
    Page<Coupon> findCouponWithDto(
            Pageable pageable,
            @Param("name") String name,
            @Param("code") String code,
            @Param("startDate") LocalDateTime startDateTime,
            @Param("endDate") LocalDateTime endDateTime);

    // 단일 쿠폰확인 fetch join
    @Query("select c from Coupon c where c.code = :couponCode")
    Coupon findByCouponCode(@Param("couponCode") String couponCode);

    // 유효기간이 지난 쿠폰 조회
    @Query("select c from Coupon c where c.endDate < :now")
    List<Coupon> findByEndAtBefore(@Param("now") LocalDate now);
}
