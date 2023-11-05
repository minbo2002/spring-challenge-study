package com.example.springchallenge2week.domain.coupon.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CouponHistoryStatus {

    USED("사용"),
    UNUSED("미사용"),
    EXPIRED("만료됨");

    private final String description;
}
