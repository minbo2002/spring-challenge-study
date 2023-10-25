package com.example.springchallenge2week.domain.coupon.repository;

import com.example.springchallenge2week.domain.coupon.entity.CouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {

}
