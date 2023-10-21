package com.example.springchallenge2week.domain.dto.request;

import com.example.springchallenge2week.domain.entity.Coupon;
import com.example.springchallenge2week.domain.entity.CouponStatus;
import com.example.springchallenge2week.domain.entity.CouponType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CouponCreateRequestDto {

    private String name;
    private String code;

    private CouponType type;

    private CouponStatus status;

    // 유효 기간
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public CouponCreateRequestDto(String name, String code, CouponType type, CouponStatus status, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.code = code;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Coupon toEntity() {
        return Coupon.builder()
                .name(name)
                .code(code)
                .type(CouponType.DISCOUNT)
                .status(CouponStatus.PUBLIC)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
