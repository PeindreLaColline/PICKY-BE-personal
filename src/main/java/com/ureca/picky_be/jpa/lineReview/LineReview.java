package com.ureca.picky_be.jpa.lineReview;

import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "line_review",
        uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id", "user_id"})
)
public class LineReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="movie_id", nullable = false)
    private Long movieId;

    @ColumnDefault("0.0")
    private double rating;

    @Column(nullable = false)
    private String context;

    @Column(nullable = false)
    private Boolean isSpoiler = false;

    private boolean isDeleted;

    @Column(nullable=false)
    private String writerNickname;

    public void lineReviewContextUpdate(String context, Boolean isSpoiler) {
        this.context = context;
        this.isSpoiler = isSpoiler;
    }
}
