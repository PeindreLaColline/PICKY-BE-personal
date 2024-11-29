package com.ureca.picky_be.jpa.lineReview;


import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "line_review_like",
        uniqueConstraints = @UniqueConstraint(columnNames = {"line_review_id", "user_id"})
)
public class LineReviewLike extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="line_review_id")
    private LineReview lineReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Preference preference;


    private boolean isDeleted;

    public LineReviewLike updatePreference(Preference newPreference) {
        this.preference = newPreference;
        return this;
    }


}
