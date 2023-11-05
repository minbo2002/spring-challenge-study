package com.example.springchallenge2week.domain.coupon.entity;

import com.example.springchallenge2week.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_history")
public class CouponHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_history_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponHistoryStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponHistoryType type;

    @Column(name = "used_at")
    private LocalDateTime usedAt;  // 쿠폰 사용일자

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;  // 쿠폰 발급일자 now() 처리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Builder
    public CouponHistory(Long id, CouponHistoryStatus status, CouponHistoryType type, LocalDateTime usedAt, LocalDateTime issuedAt, User user, Coupon coupon) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.usedAt = usedAt;
        this.issuedAt = issuedAt;
        this.user = user;
        this.coupon = coupon;
    }

    public void updateStatus(CouponHistoryStatus status) {
        this.status = CouponHistoryStatus.EXPIRED;
    }
}
