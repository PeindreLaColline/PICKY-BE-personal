package com.ureca.picky_be.jpa.movie;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.playlist.Playlist;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoviePlayList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;

    @Column(name="movie_id", nullable=false)
    private Long movieId;


}
