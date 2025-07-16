package com.example.ssru.pointhistory.entity;

import com.example.ssru.auditing.Auditing;
import com.example.ssru.history.applicationhistory.entity.ApplicationHistory;
import com.example.ssru.history.sstealhistory.entity.SstealHistory;
import com.example.ssru.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PointHistory extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String method;

    @Column
    private int amount;

    @Column
    private String situation;

    @Column
    private String merchantUid;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "application_id")
    private ApplicationHistory applicationHistory;

    @OneToOne
    @JoinColumn(name = "ssteal_id")
    private SstealHistory sstealHistory;
}
