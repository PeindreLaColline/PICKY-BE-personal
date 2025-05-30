package com.ureca.picky_be.base.implementation.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.picky_be.base.business.board.dto.*;
import com.ureca.picky_be.base.business.board.dto.boardDto.GetBoardInfoResp;
import com.ureca.picky_be.base.business.board.dto.commentDto.GetAllBoardCommentsResp;
import com.ureca.picky_be.base.business.board.dto.contentDto.AddBoardContentReq;
import com.ureca.picky_be.base.business.board.dto.contentDto.BoardContentWithBoardId;
import com.ureca.picky_be.base.business.board.dto.contentDto.GetBoardContentResp;
import com.ureca.picky_be.base.business.movie.dto.GetMovieDetailResp;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.entity.genre.Genre;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BoardDtoMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<GetBoardContentResp> mapContent(String contentJson) {
        if(contentJson == null || contentJson.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(contentJson, new TypeReference<List<GetBoardContentResp>>() {});
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BOARD_CONTENT_JSON_TRANSFERING_FAILED);
        }

    }

    public Slice<GetBoardInfoResp> toGetBoardInfoResps(
            Slice<BoardProjection> recentBoards,
            List<BoardContentWithBoardId> boardContentWithBoardIds,
            List<String> profileUrls,
            List<List<Genre>> genres
    ) {
        List<List<GetMovieDetailResp.MovieInfo.GenreInfo>> genreInfoLists = genres.stream()
                .map(genreList -> genreList.stream()
                        .map(genre -> new GetMovieDetailResp.MovieInfo.GenreInfo(genre.getId()))
                        .toList()
                )
                .toList();


        Map<Long, List<GetBoardContentResp>> boardContentMap = boardContentWithBoardIds.stream()
                .collect(Collectors.groupingBy(
                        BoardContentWithBoardId::boardId,
                        Collectors.mapping(
                                content -> new GetBoardContentResp(content.contentUrl(), content.boardContentType().toString()),
                                Collectors.toList()
                        )
                ));

        AtomicInteger index = new AtomicInteger(0);

        return recentBoards.map(board -> {
            int currentIndex = index.getAndIncrement();
            List<GetBoardContentResp> contents = boardContentMap.getOrDefault(board.getBoardId(), Collections.emptyList());

            String transformedProfileUrl = profileUrls.get(currentIndex);
            List<GetMovieDetailResp.MovieInfo.GenreInfo> genreInfo = genreInfoLists.get(currentIndex);

            return new GetBoardInfoResp(
                    board.getBoardId(),
                    board.getWriterId(),
                    board.getWriterNickname(),
                    transformedProfileUrl,
                    board.getWriterRole(),
                    board.getContext(),
                    board.getIsSpoiler(),
                    board.getCreatedAt(),
                    board.getUpdatedAt(),
                    board.getLikeCount(),
                    board.getCommentCount(),
                    contents,
                    board.getMovieId(),
                    board.getMovieName(),
                    board.getReleaseDate(),
                    genreInfo,
                    board.getIsLike(),
                    board.getIsAuthor()
            );
        });
    }

    public Slice<GetBoardInfoResp> toGetBoardInfoListGenresResps(
            Slice<BoardProjection> recentBoards,
            List<BoardContentWithBoardId> boardContentWithBoardIds,
            List<String> profileUrls,
            List<Genre> genres
    ) {
        List<GetMovieDetailResp.MovieInfo.GenreInfo> genreInfoList = genres.stream()
                .map(genre -> new GetMovieDetailResp.MovieInfo.GenreInfo(genre.getId()))
                .toList();
        Map<Long, List<GetBoardContentResp>> boardContentMap = boardContentWithBoardIds.stream()
                .collect(Collectors.groupingBy(
                        BoardContentWithBoardId::boardId,
                        Collectors.mapping(
                                content -> new GetBoardContentResp(content.contentUrl(), content.boardContentType().toString()),
                                Collectors.toList()
                        )
                ));

        AtomicInteger index = new AtomicInteger(0);

        return recentBoards.map(board -> {
            List<GetBoardContentResp> contents = boardContentMap.getOrDefault(board.getBoardId(), Collections.emptyList());

            String transformedProfileUrl = profileUrls.get(index.getAndIncrement());
            return new GetBoardInfoResp(
                    board.getBoardId(),
                    board.getWriterId(),
                    board.getWriterNickname(),
                    transformedProfileUrl,
                    board.getWriterRole(),
                    board.getContext(),
                    board.getIsSpoiler(),
                    board.getCreatedAt(),
                    board.getUpdatedAt(),
                    board.getLikeCount(),
                    board.getCommentCount(),
                    contents,
                    board.getMovieId(),
                    board.getMovieName(),
                    board.getReleaseDate(),
                    genreInfoList,
                    board.getIsLike(),
                    board.getIsAuthor()
            );
        });
    }

/*    public GetBoardInfoResp toGetBoardInfoResp(BoardProjection projection) {
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
    }*/

    public GetAllBoardCommentsResp toGetBoardCommentsInfoResp(BoardCommentProjection projection) {
        return new GetAllBoardCommentsResp(
                projection.getCommentId(),
                projection.getWriterId(),
                projection.getWriterNickname(),
                projection.getWriterProfileUrl(),
                projection.getContext(),
                projection.getCreatedAt(),
                projection.getUpdatedAt(),
                projection.getIsAuthor()
        );
    }

    public List<AddBoardContentReq> toAddBoardContentReq(List<String> images, List<String> videos) {
        List<AddBoardContentReq> imageContents = images != null
                ? images.stream()
                .map(image -> new AddBoardContentReq(image, "IMAGE"))
                .toList()
                : List.of();

        List<AddBoardContentReq> videoContents = videos != null
                ? videos.stream()
                .map(video -> new AddBoardContentReq(video, "VIDEO"))
                .toList()
                : List.of();

        return Stream.concat(imageContents.stream(), videoContents.stream())
                .toList();
    }

}
