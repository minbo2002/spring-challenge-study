package com.example.springchallenge2week.domain.user.entity;

import com.example.springchallenge2week.common.BaseTimeEntity;
import com.example.springchallenge2week.domain.coupon.entity.CouponHistory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @OneToMany(mappedBy = "user")
    private List<CouponHistory> couponHistoryies;

    @Builder
    public User(Long id, String name, String password, String email, RoleType role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
