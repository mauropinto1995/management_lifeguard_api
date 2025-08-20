package com.braveheart.gestao_ns_api.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "schedule",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_schedule_post_date_shift",
                        columnNames = {"post_id", "date", "shift_id"}
                ),
                @UniqueConstraint(
                        name = "uk_schedule_user_date_shift",
                        columnNames = {"user_id", "date", "shift_id"}
                )
        }
)
public class Schedule extends AbstractIDModel{

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;
}
