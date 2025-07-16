package com.example.ssru.history.applicationhistory.entity;

import com.example.ssru.coupon.entity.Coupon;
import com.example.ssru.option.entity.Option;
import com.example.ssru.pointhistory.entity.PointHistory;
import com.example.ssru.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class ApplicationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String progress;

    @Column
    private int price;

    @Column
    private String address;

    @Column
    private String detailAddress;

    @Column
    private String photo;

    @Column
    private String comment;

    @CreatedDate
    @Column(updatable = false)
    private Timestamp created;

    @LastModifiedDate
    @Column
    private Timestamp updated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "applicationHistory", cascade = CascadeType.ALL)
    private List<Option> optionList;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @OneToOne(mappedBy = "applicationHistory")
    private PointHistory pointHistory;
}