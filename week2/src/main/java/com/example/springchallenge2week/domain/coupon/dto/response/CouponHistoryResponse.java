package com.example.springchallenge2week.domain.coupon.dto.response;

import com.example.springchallenge2week.domain.coupon.entity.CouponHistory;
import com.example.springchallenge2week.domain.coupon.entity.CouponHistoryStatus;
import com.example.springchallenge2week.domain.coupon.entity.CouponHistoryType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
public class CouponHistoryResponse {

    private Long id;
    private CouponHistoryStatus status;  // USED(사용)  UNUSED(미사용)  EXPIRED(만료됨)
    private CouponHistoryType type;      // YES(삭제됨) NO(삭제되지않음)
    private LocalDateTime usedAt;    // 쿠폰 사용일자
    private LocalDateTime issuedAt;  // 쿠폰 발급일자 now() 처리
    private Long userId;
    private Long couponId;

    @Builder
    public CouponHistoryResponse(Long id, CouponHistoryStatus status, CouponHistoryType type, LocalDateTime usedAt, LocalDateTime issuedAt, Long userId, Long couponId) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.usedAt = usedAt;
        this.issuedAt = issuedAt;
        this.userId = userId;
        this.couponId = couponId;
    }

    static public CouponHistoryResponse toDto(CouponHistory couponHistory) {
        return CouponHistoryResponse.builder()
                .id(couponHistory.getId())
                .status(couponHistory.getStatus())
                .type(couponHistory.getType())
                .usedAt(couponHistory.getUsedAt())
                .issuedAt(couponHistory.getIssuedAt())
                .userId(couponHistory.getUser().getId())
                .couponId(couponHistory.getCoupon().getId())
                .build();
    }
}
