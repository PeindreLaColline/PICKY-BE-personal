package com.ureca.picky_be.jpa.entity.movie;

import com.ureca.picky_be.jpa.entity.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieGenre extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="movie_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Movie movie;

    @Column(nullable = false)
    private Long genreId;


}
