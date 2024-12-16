package com.ureca.picky_be.base.business.board;

import com.ureca.picky_be.base.business.board.dto.*;
import com.ureca.picky_be.base.business.board.dto.boardDto.AddBoardReq;
import com.ureca.picky_be.base.business.board.dto.boardDto.BoardMovieIdQueryReq;
import com.ureca.picky_be.base.business.board.dto.boardDto.GetBoardInfoResp;
import com.ureca.picky_be.base.business.board.dto.boardDto.UpdateBoardReq;
import com.ureca.picky_be.base.business.board.dto.commentDto.AddBoardCommentReq;
import com.ureca.picky_be.base.business.board.dto.commentDto.GetAllBoardCommentsResp;
import com.ureca.picky_be.base.business.board.dto.contentDto.BoardContentWithBoardId;
import com.ureca.picky_be.base.business.notification.dto.BoardCreatedEvent;
import com.ureca.picky_be.base.business.user.dto.BoardQueryReq;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.board.BoardManager;
import com.ureca.picky_be.base.implementation.content.ImageManager;
import com.ureca.picky_be.base.implementation.content.ProfileManager;
import com.ureca.picky_be.base.implementation.content.VideoManager;
import com.ureca.picky_be.base.implementation.mapper.BoardDtoMapper;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.board.Board;
import com.ureca.picky_be.jpa.entity.board.BoardContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService implements BoardUseCase {

    private final BoardManager boardManager;
    private final AuthManager authManager;
    private final BoardDtoMapper boardDtoMapper;
    private final ImageManager imageManager;
    private final VideoManager videoManager;
    private final ProfileManager profileManager;
    private final ApplicationEventPublisher eventPublisher;
    private final UserManager userManager;

    @Override
    @Transactional
    public SuccessCode addBoard(AddBoardReq req, List<MultipartFile> images, List<MultipartFile> videos) throws IOException {
        Long userId = authManager.getUserId();
        String userNickname = authManager.getUserNickname();
        if ((images == null || images.isEmpty()) && (videos == null || videos.isEmpty())) {
            throw new CustomException(ErrorCode.MISSING_BOARD_CONTENT);
        } else if (images != null && images.size() > 3) {
            throw new CustomException(ErrorCode.BOARD_CONTENT_TOO_MANY);
        } else if (videos != null && videos.size() > 2) {
            throw new CustomException(ErrorCode.BOARD_CONTENT_TOO_MANY);
        }
        List<String> imageUrls = imageManager.uploadImages(images);
        List<String> videosUrls = videoManager.uploadVideo(videos);

        Board board = boardManager.addBoard(userId, userNickname, req, boardDtoMapper.toAddBoardContentReq(imageUrls, videosUrls));

        log.info("Event publish start");
        eventPublisher.publishEvent(new BoardCreatedEvent(board.getUserId(), req.movieId(), board.getId()));
        log.info("Event publish end");
        return SuccessCode.CREATE_BOARD_SUCCESS;
    }

    @Override
    @Transactional
    public void updateBoard(Long boardId , UpdateBoardReq req) {
        Long userId = authManager.getUserId();
        boardManager.checkBoardWriteUser(boardId, userId);
        boardManager.updateBoard(boardId, userId, req);
    }

    @Override
    public Slice<GetBoardInfoResp> getBoards(Pageable pageable, Long lastBoardId) {
        Long userId = authManager.getUserId();
        Slice<BoardProjection> recentBoards = boardManager.getRecentMovieBoards(userId, lastBoardId, pageable);
        List<String> profileUrls = recentBoards.getContent().stream()
                .map(BoardProjection::getWriterProfileUrl)
                .map(url -> url != null ? profileManager.getPresignedUrl(url) : null)
                .toList();
        List<Long> boardIds = recentBoards.getContent().stream()
                .map(BoardProjection::getBoardId)
                .toList();
        List<BoardContentWithBoardId> boardContentWithBoardIds = processBoardContents(boardManager.getBoardContentWithBoardId(boardIds));
        return boardDtoMapper.toGetBoardInfoResps(recentBoards, boardContentWithBoardIds, profileUrls);
    }

    @Override
    public Slice<GetBoardInfoResp> getMovieRelatedBoards(Pageable pageable, BoardMovieIdQueryReq req) {
        Long userId = authManager.getUserId();
        Slice<BoardProjection> recentMovieRelatedBoards = boardManager.getRecentMovieRelatedBoards(userId, req.movieId(), req.lastBoardId(), pageable);
        List<String> profileUrls = recentMovieRelatedBoards.getContent().stream()
                .map(BoardProjection::getWriterProfileUrl)
                .map(profileManager::getPresignedUrl)
                .toList();
        List<Long> boardIds = recentMovieRelatedBoards.getContent().stream()
                .map(BoardProjection::getBoardId)
                .toList();
        List<BoardContentWithBoardId> boardContentWithBoardIds = processBoardContents(boardManager.getBoardContentWithBoardId(boardIds));
        return boardDtoMapper.toGetBoardInfoResps(recentMovieRelatedBoards, boardContentWithBoardIds, profileUrls);
    }

    public List<BoardContentWithBoardId> processBoardContents(List<BoardContentWithBoardId> boardContentWithBoardIds) {
        return boardContentWithBoardIds.stream()
                .map(content -> {
                    String updatedUrl;

                    if (content.boardContentType() == BoardContentType.IMAGE) {
                        updatedUrl = imageManager.getPresignedUrl(content.contentUrl());
                    } else if (content.boardContentType() == BoardContentType.VIDEO) {
                        updatedUrl = videoManager.getPresignedUrl(content.contentUrl());
                    } else {
                        throw new CustomException(ErrorCode.INVALID_CONTENT_TYPE);
                    }

                    return new BoardContentWithBoardId(
                            content.boardId(),
                            updatedUrl,
                            content.boardContentType()
                    );
                })
                .toList();
    }

    @Override
    public void addBoardComment(AddBoardCommentReq req, Long boardId) {
        Long userId = authManager.getUserId();
        String userNickname = authManager.getUserNickname();
        boardManager.addBoardComment(req.content(), boardId, userId, userNickname);
    }

    @Override
    public Slice<GetAllBoardCommentsResp> getAllBoardComments(Pageable pageable, Long boardId, Long lastCommentId) {
        Long userId = authManager.getUserId();
        Slice<BoardCommentProjection> comments = boardManager.getTenBoardCommentsPerReq(userId, boardId, lastCommentId, pageable);
        return comments.map(boardDtoMapper::toGetBoardCommentsInfoResp);
    }

    @Override
    public void deleteBoard(Long boardId) {
        Long userId = authManager.getUserId();
        boardManager.checkBoardWriteUser(boardId, userId);
        boardManager.deleteBoard(boardId);
    }

    @Override
    public void deleteBoardComment(Long boardId, Long commentId) {
        Long userId = authManager.getUserId();
        boardManager.checkBoardIsDeleted(boardId);
        boardManager.checkBoardCommentWriteUser(commentId, userId);
        boardManager.deleteBoardComment(commentId);

    }



    @Override
    public boolean createBoardLike(Long boardId) {
        Long userId = authManager.getUserId();
        boardManager.checkBoardIsDeleted(boardId);
        if(boardManager.checkUserBoardLike(userId,boardId)) {   // 눌려 있다면
            // 좋아요 delete
            boardManager.deleteBoardLike(userId,boardId);
            return false;
        } else {        // 좋아요 없으면
            // 좋아요 Create
            Board board = boardManager.getBoardById(boardId);
            boardManager.createBoardLike(userId, board);
            return true;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<GetBoardInfoResp> getBoardsByNickName(PageRequest pageRequest, BoardQueryReq req) {
        Long userId = userManager.getUserIdByNickname(req.nickname());
        Slice<BoardProjection> boards = boardManager.findBoardsByUserId(userId, req, pageRequest);
        List<String> profileUrls = boards.getContent().stream()
                .map(BoardProjection::getWriterProfileUrl)
                .map(url -> url != null ? profileManager.getPresignedUrl(url) : null)
                .toList();
        List<Long> boardIds = boards.getContent().stream()
                .map(BoardProjection::getBoardId)
                .toList();
        List<BoardContentWithBoardId> boardContentWithBoardIds = processBoardContents(boardManager.getBoardContentWithBoardId(boardIds));
        return boardDtoMapper.toGetBoardInfoResps(boards, boardContentWithBoardIds , profileUrls);
    }
}
