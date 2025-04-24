package com.stepthrone.stepcount.model;


import com.stepthrone.userprofile.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "daily_steps",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_date",
                columnNames = {"user_id", "date"}
        ))
@Data
@NoArgsConstructor
public class DailyStepData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer steps;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Instant lastUpdated = Instant.now();



}