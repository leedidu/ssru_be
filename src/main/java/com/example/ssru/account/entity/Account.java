package com.example.ssru.account.entity;

import com.example.ssru.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String bank;

    @Column
    private String account;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
