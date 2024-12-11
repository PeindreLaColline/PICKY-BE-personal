package com.ureca.picky_be.base.business.board;

import com.ureca.picky_be.base.business.board.dto.*;
import com.ureca.picky_be.base.business.board.dto.boardDto.AddBoardReq;
import com.ureca.picky_be.base.business.board.dto.boardDto.GetBoardInfoResp;
import com.ureca.picky_be.base.business.board.dto.boardDto.UpdateBoardReq;
import com.ureca.picky_be.base.business.board.dto.commentDto.AddBoardCommentReq;
import com.ureca.picky_be.base.business.board.dto.commentDto.GetAllBoardCommentsResp;
import com.ureca.picky_be.base.business.board.dto.likeDto.AddOrDeleteBoardLikeResp;
import com.ureca.picky_be.base.business.notification.dto.BoardCreatedEvent;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.board.BoardManager;
import com.ureca.picky_be.base.implementation.content.ImageManager;
import com.ureca.picky_be.base.implementation.content.VideoManager;
import com.ureca.picky_be.base.implementation.mapper.BoardDtoMapper;
import com.ureca.picky_be.base.persistence.board.BoardRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.board.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public SuccessCode addBoard(AddBoardReq req, List<MultipartFile> images, List<MultipartFile> videos) throws IOException {
        Long userId = authManager.getUserId();
        String userNickname = authManager.getUserNickname();
        if(images.size() >3 || videos.size() >2) {
            throw new CustomException(ErrorCode.BOARD_CONTENT_TOO_MANY);
        }
        List<String> imageUrls = imageManager.uploadImages(images);
        List<String> videosUrls = videoManager.uploadVideo(videos);

        Board board = boardManager.addBoard(userId, userNickname, req, boardDtoMapper.toAddBoardContentReq(imageUrls, videosUrls));
//        eventPublisher.publishEvent(new BoardCreatedEvent(board.getUserId() ,req.movieId(), board.getId()));
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
    public Slice<GetBoardInfoResp> getBoards(Pageable pageable) {
        Long userId = authManager.getUserId();
        Slice<BoardProjection> recentBoards = boardManager.getRecentMovieBoards(userId, pageable);
        return recentBoards.map(boardDtoMapper::toGetBoardInfoResp);
    }

    @Override
    public Slice<GetBoardInfoResp> getMovieRelatedBoards(Long movieId, Pageable pageable) {
        Long userId = authManager.getUserId();
        Slice<BoardProjection> recentMovieRelatedBoards = boardManager.getRecentMovieRelatedBoards(userId, movieId, pageable);
        return recentMovieRelatedBoards.map(boardDtoMapper::toGetBoardInfoResp);
    }

    @Override
    public void addBoardComment(AddBoardCommentReq req, Long boardId) {
        Long userId = authManager.getUserId();
        String userNickname = authManager.getUserNickname();
        boardManager.addBoardComment(req.content(), boardId, userId, userNickname);
    }

    @Override
    public Slice<GetAllBoardCommentsResp> getAllBoardComments(Long boardId, Pageable pageable) {
        /**
         * 1. boardId에 있는 댓글들 싹 가져와
         */

        Slice<BoardCommentProjection> comments = boardManager.getTenBoardCommentsPerReq(boardId, pageable);
//        try {
//
//        } catch (Exception e){
//            throw new CustomException(ErrorCode.)
//        }
        return comments.map(boardDtoMapper::toGetBoardInfoResp);
    }

    @Override
    public void deleteBoard(Long boardId) {
        /**
         * 1. boardId로 해당 보드가 user가 작성한 건지 검증 로직
         * 2. 해당 Board 삭제(Board에 작성된 댓글, 좋아요 전부 삭제한다)
         */
        Long userId = authManager.getUserId();
        boardManager.checkBoardWriteUser(boardId, userId);
        boardManager.deleteBoard(boardId);
    }

    @Override
    public void deleteBoardComment(Long boardId, Long commentId) {
        /**
         * 1. 삭제하려는 comment가 user것인지 검사
         * 2. 일치하면 삭제
         */

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




}