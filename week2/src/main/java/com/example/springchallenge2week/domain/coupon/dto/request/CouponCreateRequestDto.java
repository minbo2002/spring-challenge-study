package com.example.springchallenge2week.domain.coupon.dto.request;

import com.example.springchallenge2week.domain.coupon.entity.Coupon;
import com.example.springchallenge2week.domain.coupon.entity.CouponStatus;
import com.example.springchallenge2week.domain.coupon.entity.CouponType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CouponCreateRequestDto {

    @NotBlank
    @Pattern(regexp = "^[가-힣0-9\\s]+$", message = "한글과 숫자만 입력 가능합니다.")
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

        if (startDate != null && startDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("쿠폰 유효시작일은 현재 날짜 이거나 과거의 날짜여야 합니다.");
        }

        if (endDate != null && endDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("쿠폰 유효마지막일은 현재 날짜 이거나 이후의 날짜여야 합니다.");
        }

        return Coupon.builder()
                .name(name)
                .code(generateRandomCode(10))
                .type(CouponType.DISCOUNT)
                .status(CouponStatus.PUBLIC)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // 대문자 알파벳만 사용
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            codeBuilder.append(randomChar);
        }

        return codeBuilder.toString();
    }
}
