package com.example.springchallenge2week.domain.coupon.entity;

import com.example.springchallenge2week.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon")
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
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

    @Column(name = "logo_image", length = 255)
    private String logoImage;

    @OneToMany(mappedBy = "coupon")
    private List<CouponHistory> couponHistoryies;

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

    public void changeLogoImage(String uploadedUrl) {
        this.logoImage = uploadedUrl;
    }
}
