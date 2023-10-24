package com.example.springchallenge2week.domain.coupon.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CouponHistoryType {

    YES("삭제됨"),
    NO("삭제되지 않음");

    private final String description;
}
