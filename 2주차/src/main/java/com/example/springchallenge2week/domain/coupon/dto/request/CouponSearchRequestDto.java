package com.example.springchallenge2week.domain.coupon.dto.request;

import com.example.springchallenge2week.common.page.PageRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CouponSearchRequestDto extends PageRequestDto {

    @NotBlank
    private String name;

    private String code;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
