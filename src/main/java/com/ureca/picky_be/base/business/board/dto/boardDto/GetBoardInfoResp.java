package com.ureca.picky_be.base.business.board.dto.boardDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ureca.picky_be.base.business.board.dto.contentDto.GetBoardContentResp;
import com.ureca.picky_be.base.business.movie.dto.GetMovieDetailResp;
import com.ureca.picky_be.jpa.entity.genre.Genre;

import java.time.LocalDateTime;
import java.util.*;

public record GetBoardInfoResp(
        Long boardId,
        Long writerId,
        String writerNickname,
        String writerProfileUrl,
        String writerRole,
        String context,
        Boolean isSpoiler,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Integer likesCount,
        Integer commentsCount,
        List<GetBoardContentResp> contents,
        Long movieId,
        String movieTitle,
        Date releaseDate,
        List<GetMovieDetailResp.MovieInfo.GenreInfo> genres,
        Boolean isLike,
        boolean isAuthor
        ) {

}
