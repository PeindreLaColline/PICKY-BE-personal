package com.ureca.picky_be.jpa.movie;

import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String movieTitle;

    @Column(nullable = false)
    private Date movieReleaseDate;

    private String moviePosterUrl;

    @Column(nullable = false)
    @ColumnDefault("0.0")
    private Double movieTotalRating;

    @Column(nullable = false)
    private String moviePlot;

    @Column(nullable = false)
    private int movieRunningTime;

    private String movieTrailerUrl;

    private String movieOstUrl;

}
