package com.example.springchallenge2week.domain.coupon.repository;

import com.example.springchallenge2week.domain.coupon.dto.request.CouponSearchRequestDto;
import com.example.springchallenge2week.domain.coupon.entity.Coupon;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.example.springchallenge2week.domain.coupon.entity.QCoupon.coupon;

@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Coupon> findCouponWithQueryDsl(CouponSearchRequestDto requestDto) {

        List<Coupon> list = queryFactory
                .selectFrom(coupon)
                .where(
                        eqCouponName(requestDto),
                        eqCouponCode(requestDto),
                        dateBetween(requestDto)
                )
                .offset(requestDto.getPageable().getOffset())
                .limit(requestDto.getPageable().getPageSize())
                .orderBy(coupon.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(
                        eqCouponName(requestDto),
                        eqCouponCode(requestDto),
                        dateBetween(requestDto)
                );

        return PageableExecutionUtils.getPage(list, requestDto.getPageable(), countQuery::fetchOne);
    }

    private static BooleanExpression eqCouponName(CouponSearchRequestDto requestDto) {
        if(!ObjectUtils.isEmpty(requestDto.getName())) {
            return coupon.name.contains(requestDto.getName());
        }
        return null;
    }

    private static BooleanExpression eqCouponCode(CouponSearchRequestDto requestDto) {
        if(!ObjectUtils.isEmpty(requestDto.getCode())) {
            return coupon.code.contains(requestDto.getCode());
        }
        return null;
    }

    private static BooleanExpression dateBetween(CouponSearchRequestDto requestDto) {
        if(!ObjectUtils.isEmpty(requestDto.getStartDate()) && !ObjectUtils.isEmpty(requestDto.getEndDate())) {
            // LocalDate타입인 startDate와 endDate를 LocalDateTime으로 변환하여 between()메소드에 매개변수로 전달
            return coupon.createdAt.between(requestDto.getStartDate().atStartOfDay(), requestDto.getEndDate().atStartOfDay());
        }
        return null;
    }
}
