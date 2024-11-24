package com.ureca.picky_be.jpa.movie;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.movieworker.MovieWorker;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class FilmCrew extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="film_crew_id")
    private MovieWorker movieWorker;

    @Enumerated(EnumType.STRING)
    private FilmCrewPosition filmCrewPosition;

    //    @Column(nullable = false)
    private String role;

}
