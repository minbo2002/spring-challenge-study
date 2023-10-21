package com.example.springchallenge2week.domain.dto.response;

import com.example.springchallenge2week.domain.entity.Coupon;
import com.example.springchallenge2week.domain.entity.CouponStatus;
import com.example.springchallenge2week.domain.entity.CouponType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
public class CouponResponse {

    private final Long id;
    private final String name;
    private final String code;
    private final CouponType type;
    private final CouponStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime modifiedAt;

    static public CouponResponse toDto(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .code(coupon.getCode())
                .type(coupon.getType())
                .status(coupon.getStatus())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .createdAt(coupon.getCreatedAt())
                .modifiedAt(coupon.getModifiedAt())
                .build();
    }
}
