package com.ureca.picky_be.jpa.movie;

import com.ureca.picky_be.base.business.movie.dto.UpdateMovieReq;
import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Date releaseDate;

    private String posterUrl;

    private String backdropUrl;

    @Column(nullable = false)
    @ColumnDefault("0.0")
    private double totalRating;

    @Column(nullable = false)
    private String plot;

    @Column(nullable = false)
    private int runningTime;

    private String trailerUrl;

    private String ostUrl;

    private boolean isDeleted;      // 삭제 여부

    @Transactional
    public Movie updateMovie(UpdateMovieReq updateMovieReq){
        if(updateMovieReq.movieInfo().title() != null) this.title = updateMovieReq.movieInfo().title();
        if(updateMovieReq.movieInfo().releaseDate() != null) this.releaseDate = updateMovieReq.movieInfo().releaseDate();
        if(updateMovieReq.movieInfo().posterUrl() != null) this.posterUrl = updateMovieReq.movieInfo().posterUrl();
        if(updateMovieReq.movieInfo().backdropUrl() != null) this.posterUrl = updateMovieReq.movieInfo().backdropUrl();
        if(updateMovieReq.movieInfo().plot() != null) this.plot = updateMovieReq.movieInfo().plot();
        //아래 부분 수정해두기
        //if(updateMovieReq.movieInfo().runtime() != null) this.runningTime = updateMovieReq.movieInfo().runtime();
        if(updateMovieReq.trailer() != null) this.trailerUrl = updateMovieReq.trailer();
        if(updateMovieReq.ost() != null) this.ostUrl = updateMovieReq.ost();
        return this;
    }
}
