package com.example.ssru.coupon.entity;

import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String couponName;

    @Column
    private Boolean whetherUse;

    @Column
    private int discount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime publish;

    @Column
    private Timestamp deadline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "coupon")
    private ApplicationHistory applicationHistory;
}