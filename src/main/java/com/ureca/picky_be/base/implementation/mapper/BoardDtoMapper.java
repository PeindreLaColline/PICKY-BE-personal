package com.ureca.picky_be.base.implementation.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.picky_be.base.business.board.dto.*;
import com.ureca.picky_be.base.business.board.dto.boardDto.GetBoardInfoResp;
import com.ureca.picky_be.base.business.board.dto.commentDto.GetAllBoardCommentsResp;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class BoardDtoMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<BoardContentPOJO> mapContent(String contentJson) {
        if(contentJson == null || contentJson.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(contentJson, new TypeReference<List<BoardContentPOJO>>() {});
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BOARD_CONTENT_JSON_TRANSFERING_FAILED);
        }

    }

    public GetBoardInfoResp toGetBoardInfoResp(BoardProjection projection) {
        return new GetBoardInfoResp(
                projection.getBoardId(),
                projection.getWriterId(),
                projection.getWriterNickname(),
                projection.getWriterProfileUrl(),
                projection.getContext(),
                projection.getIsSpoiler(),
                projection.getCreatedAt(),
                projection.getUpdatedAt(),
                projection.getLikeCount(),
                projection.getCommentCount(),
                mapContent(projection.getContents()),
                projection.getMovieName(),
                projection.getIsLike()
        );
    }

    public GetAllBoardCommentsResp toGetBoardInfoResp(BoardCommentProjection projection) {
        return new GetAllBoardCommentsResp(
                projection.getCommentId(),
                projection.getWriterId(),
                projection.getWriterNickname(),
                projection.getWriterProfileUrl(),
                projection.getContext(),
                projection.getCreatedAt(),
                projection.getUpdatedAt()
        );
    }

}
