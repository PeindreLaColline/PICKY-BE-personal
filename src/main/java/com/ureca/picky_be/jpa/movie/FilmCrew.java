package com.ureca.picky_be.jpa.movie;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.movieworker.MovieWorker;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilmCrew extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="movie_worker_id")
    private MovieWorker movieWorkerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FilmCrewPosition filmCrewPosition;

    //    @Column(nullable = false)
    private String role;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

}
