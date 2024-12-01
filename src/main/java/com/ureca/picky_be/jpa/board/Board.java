package com.ureca.picky_be.jpa.board;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.movie.Movie;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

//    @Column(name = "movie_id", nullable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", orphanRemoval = true)
    private List<BoardContent> contents = new ArrayList<>();

    private String context;

    private boolean isSpoiler;


    public void updateBoard(Movie movie, String context, boolean isSpoiler) {
        this.movie = movie;
        this.context = context;
        this.isSpoiler = isSpoiler;
    }

    public static Board of(Long userId, Movie movie, List<BoardContent> contents, String context, boolean isSpoiler) {
        return Board.builder()
                .userId(userId)
                .movie(movie)
                .contents(contents)
                .context(context)
                .isSpoiler(isSpoiler)
                .build();
    }

    public void addContent(BoardContent content) {
        this.contents.add(content);
    }
}
