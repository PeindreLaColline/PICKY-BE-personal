package com.ureca.picky_be.jpa.board;


import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String context;

    private String writerNickname;



    public static BoardComment of(Board board, Long userId, String context, String writerNickname){
        return BoardComment.builder()
                .context(context)
                .board(board)
                .writerNickname(writerNickname)
                .userId(userId)
                .build();
    }
}
