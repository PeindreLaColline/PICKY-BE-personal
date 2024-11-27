package com.ureca.picky_be.jpa.linereview;

import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
public class LineReviewSoftDelete extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="line_review_id", nullable = false)
    private Long lineReviewId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="movie_id", nullable = false)
    private Long movieId;

    @ColumnDefault("0.0")
    private double rating;

    @Column(nullable = false)
    private String context;


}
