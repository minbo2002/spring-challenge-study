package com.example.springchallenge2week.domain.user.entity;

import com.example.springchallenge2week.common.BaseTimeEntity;
import com.example.springchallenge2week.common.security.jwt.entity.Token;
import com.example.springchallenge2week.domain.coupon.entity.CouponHistory;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@ToString(exclude = {"roles"})  // 양방향 매핑에서 무한루프에 의한 StackOverflowError 방지를 위해 멤버변수인 roles를 @ToString에서 제외
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    public enum RoleType {
        USER, ADMIN
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponHistory> couponHistoryies;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Token> tokens;

    // 복수개로 Role 관리, 중간매핑 테이블 구현
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles;

    @Builder
    public User(Long id, String name, String email, String password, Address address, RoleType role, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
        this.roles = roles;
    }
}
