package com.example.springchallenge2week.domain.repository;

import com.example.springchallenge2week.domain.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 쿠폰 리스트 + 페이징 + 검색
    @Query("select c from Coupon c where c.name like %:name% or c.code like %:code% or c.createdAt BETWEEN :startDate AND :endDate")
    Page<Coupon> findCouponWithParam(
            Pageable pageable,
            @Param("name") String name,
            @Param("code") String code,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 쿠폰 리스트 + DTO(페이징) + DTO(검색)
    @Query("select c from Coupon c where c.name like %:name% or c.code like %:code% or c.createdAt BETWEEN :startDate AND :endDate")
    Page<Coupon> findCouponWithDto(
            Pageable pageable,
            @Param("name") String name,
            @Param("code") String code,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
