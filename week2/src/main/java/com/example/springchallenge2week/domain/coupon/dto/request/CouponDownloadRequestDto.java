package com.example.springchallenge2week.domain.coupon.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CouponDownloadRequestDto {

    @NotBlank
    @Size(min = 10, max = 10, message = "쿠폰코드는 10글자입니다.")
    @Pattern( regexp = "^[A-Z]+$", message = "대문자영어만 입력가능합니다")
    private String couponCode;

    @Builder
    public CouponDownloadRequestDto(String couponCode) {
        this.couponCode = couponCode;
    }
}
