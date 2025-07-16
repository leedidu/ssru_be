package com.example.ssru.address.entity;

import com.example.ssru.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String address;

    @Column
    private String detailAddress;

    @Column
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}