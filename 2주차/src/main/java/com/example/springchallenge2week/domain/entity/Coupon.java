package com.example.springchallenge2week.domain.entity;

import com.example.springchallenge2week.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon")
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    // 유효 기간
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @Builder
    public Coupon(Long id, String name, String code, CouponType type, CouponStatus status, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
