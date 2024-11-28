package com.ureca.picky_be.jpa.platform;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.movie.Movie;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Platform extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlatformType platformType;


    private String url;

}
