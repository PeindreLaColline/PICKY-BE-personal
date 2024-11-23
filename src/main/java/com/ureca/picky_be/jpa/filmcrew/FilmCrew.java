package com.ureca.picky_be.jpa.filmcrew;

import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class FilmCrew extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filmCrewName;

    private String filmCrewProfileUrl;
}
