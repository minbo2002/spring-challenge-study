package com.example.springchallenge2week.domain.coupon.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CouponStatus {

    PRIVATE("비활성화"),
    PUBLIC("활성화");

    private final String description;
}
