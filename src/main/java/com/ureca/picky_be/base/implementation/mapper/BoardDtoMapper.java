package com.ureca.picky_be.base.implementation.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.picky_be.base.business.board.dto.BoardContentProjection;
import com.ureca.picky_be.base.business.board.dto.BoardProjection;
import com.ureca.picky_be.base.business.board.dto.GetBoardContentsResp;
import com.ureca.picky_be.base.business.board.dto.GetBoardInfoResp;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardDtoMapper {

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
//                mapContent(projection.getContents()),
//                mapContent(parseContents(projection.getContents())),
                projection.getMovieName(),
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<BoardContentProjection> parseContents(String contentsJson) {
        try {
            return objectMapper.readValue(contentsJson, new TypeReference<List<BoardContentProjection>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse contents JSON", e);
        }
    }



}
