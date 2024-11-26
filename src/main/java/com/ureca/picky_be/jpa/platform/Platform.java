package com.ureca.picky_be.jpa.platform;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.movie.Movie;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Platform extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Enumerated(EnumType.STRING)
    private PlatformType platformType;


    private String url;

}
