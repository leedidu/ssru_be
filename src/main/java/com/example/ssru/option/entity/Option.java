package com.example.ssru.option.entity;

import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.sstealhistory.entity.SstealHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@Table(name = "option_list")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String type;

    @Column
    private String detail;

    @Column
    private int count;

    @Column
    private int price;

    @Column
    private String progress;

    @ManyToOne
    @JoinColumn(name = "ssteal_id")
    private SstealHistory sstealHistory;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private ApplicationHistory applicationHistory;
}