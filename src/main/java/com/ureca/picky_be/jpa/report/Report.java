package com.ureca.picky_be.jpa.report;


import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double reviewId;

    @Column(nullable = false)
    private Review reviewType;

    @Column(nullable = false)
    private String reportReason;

    @Column(nullable = false)
    private ReportType reportType;



}
