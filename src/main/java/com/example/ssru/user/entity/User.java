package com.example.ssru.user.entity;

import com.example.ssru.account.entity.Account;
import com.example.ssru.auditing.Auditing;
import com.example.ssru.coupon.entity.Coupon;
import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.sstealhistory.entity.SstealHistory;
import com.example.ssru.pointhistory.entity.PointHistory;
import com.example.ssru.address.entity.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String loginId;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String loginPw;

    @Column
    private String name;

    @Column
    @Pattern(regexp = "^010-?([0-9]{4})-?([0-9]{4})$",
            message = "전화번호 형태는 010-1234-1234 입니다.")
    private String phone;

    @Column
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
            message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Column
    private int point;

    @Column
    private String nickname;

    @Column
    private String refreshToken;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column
    private LocalDateTime updated;

    @Column
    private String auth;

    @Column
    private String billPw;

    @OneToMany(mappedBy = "user")
    private List<ApplicationHistory> applicationHistories;

    @OneToMany(mappedBy = "sstealer")
    private List<SstealHistory> sstealHistories;

    @OneToMany(mappedBy = "user")
    private List<Coupon> coupons;

    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    @OneToMany(mappedBy = "user")
    private List<PointHistory> pointHistories;
}