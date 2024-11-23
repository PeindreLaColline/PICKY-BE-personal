package com.ureca.picky_be.jpa.movie;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.filmcrew.FilmCrew;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class MovieCrew extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="film_crew_id")
    private FilmCrew filmCrew;

    @Enumerated(EnumType.STRING)
    private MovieCrewPosition movieCrewPosition;

    //    @Column(nullable = false)
    private String movieCrewRole;

}
