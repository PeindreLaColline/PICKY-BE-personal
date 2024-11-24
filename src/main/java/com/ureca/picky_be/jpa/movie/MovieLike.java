package com.ureca.picky_be.jpa.movie;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
