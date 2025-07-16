package com.example.ssru.history.sstealhistory.entity;

import com.example.ssru.chattingroom.entity.ChattingRoom;
import com.example.ssru.option.entity.Option;
import com.example.ssru.pointhistory.entity.PointHistory;
import com.example.ssru.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class SstealHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Timestamp estimatedDate;

    @Column
    private String photo;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column
    private LocalDateTime updated;

    @Column
    private String progress;

    @Column
    private String cancelReason;

    @ManyToOne
    @JoinColumn(name = "sstealer_id")
    private User sstealer;

    @OneToOne(mappedBy = "sstealHistory")
    private ChattingRoom chattingRoom;

    @OneToMany(mappedBy = "sstealHistory")
    private List<Option> optionList;

    @OneToOne(mappedBy = "sstealHistory")
    private PointHistory pointHistory;
}
