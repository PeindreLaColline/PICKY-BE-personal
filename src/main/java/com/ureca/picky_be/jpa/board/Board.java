package com.ureca.picky_be.jpa.board;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.movie.Movie;
import com.ureca.picky_be.jpa.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name = "movie_id", nullable=false)
    private Long movieId;

    private String context;

}
