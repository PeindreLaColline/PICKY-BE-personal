package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.board.dto.BoardContentProjection;
import com.ureca.picky_be.base.business.board.dto.BoardProjection;
import com.ureca.picky_be.base.business.board.dto.GetBoardContentsResp;
import com.ureca.picky_be.base.business.board.dto.GetBoardInfoResp;
import com.ureca.picky_be.jpa.board.Board;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardDtoMapper {

    public GetBoardInfoResp toGetBoardInfoResp(BoardProjection projection) {
        return new GetBoardInfoResp(
                projection.getID(),
                projection.getWriterId(),
                projection.getWriterNickname(),
                projection.getWriterProfileUrl(),
                projection.getContext(),
                projection.getIsSpoiler(),
                projection.getCreatedDate(),
                projection.getUpdatedDate(),
                projection.getLikeCount(),
                projection.getCommentCount(),
                mapContent(projection.getContents()),
                projection.getMovieTitle(),
                projection.getIsLike()
        );
    }

    private List<GetBoardContentsResp> mapContent(List<BoardContentProjection> contentProjection) {
        if(contentProjection == null) {
            return List.of();
        }
        return contentProjection.stream()
                .map(content -> new GetBoardContentsResp(
                        content.getContentUrl(),
                        content.getBoardContentType()
                )).collect(Collectors.toList());
    }



}
