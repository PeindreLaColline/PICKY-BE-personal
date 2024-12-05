package com.ureca.picky_be.jpa.board;

import com.ureca.picky_be.base.business.board.dto.contentDto.AddBoardContentReq;
import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.config.IsDeleted;
import com.ureca.picky_be.jpa.movie.Movie;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardContent> contents = new ArrayList<>();

    private String context;

    private boolean isSpoiler;

    private String writerNickname;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FALSE'")
    private IsDeleted isDeleted;      // 삭제 여부

    public void updateBoard(String context, boolean isSpoiler) {
        this.context = context;
        this.isSpoiler = isSpoiler;
    }

    public static Board of(Long userId, Movie movie, String context, boolean isSpoiler, List<AddBoardContentReq> addBoardContentReqs, String writerNickname) {

        Board board = Board.builder()
                .userId(userId)
                .movie(movie)
                .context(context)
                .isSpoiler(isSpoiler)
                .writerNickname(writerNickname)
                .isDeleted(IsDeleted.FALSE)
                .contents(new ArrayList<>())
                .build();

        for (AddBoardContentReq dto : addBoardContentReqs) {
            BoardContent boardContent = BoardContent.of(board, dto.contentUrl(), dto.type());
            board.addBoardContent(boardContent);
        }
        return board;
    }

    public void addBoardContent(BoardContent boardContent) {
        this.contents.add(boardContent);
    }

    public void deleteBoard(IsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }
}
