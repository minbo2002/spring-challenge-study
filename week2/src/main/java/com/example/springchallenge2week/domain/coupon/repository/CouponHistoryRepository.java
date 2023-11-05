package com.example.springchallenge2week.domain.coupon.repository;

import com.example.springchallenge2week.domain.coupon.entity.Coupon;
import com.example.springchallenge2week.domain.coupon.entity.CouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {

    @Query("select ch from CouponHistory ch where ch.user.id = :userId and ch.coupon.id = :couponId")
    CouponHistory findByUserAndCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);

    @Query("select ch from CouponHistory ch where ch.coupon.id = :couponId")
    List<CouponHistory> findByCouponId(@Param("couponId") Long couponId);

}
