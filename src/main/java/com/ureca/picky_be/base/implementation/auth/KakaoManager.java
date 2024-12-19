package com.ureca.picky_be.base.implementation.auth;

import com.ureca.picky_be.base.business.auth.dto.*;
import com.ureca.picky_be.base.persistence.board.BoardCommentRepository;
import com.ureca.picky_be.base.persistence.board.BoardLikeRepository;
import com.ureca.picky_be.base.persistence.board.BoardRepository;
import com.ureca.picky_be.base.persistence.lineReview.LineReviewLikeRepository;
import com.ureca.picky_be.base.persistence.lineReview.LineReviewRepository;
import com.ureca.picky_be.base.persistence.lineReview.LineReviewSoftDeleteRepository;
import com.ureca.picky_be.base.persistence.movie.MovieLikeRepository;
import com.ureca.picky_be.base.persistence.user.UserGenrePreferenceRepository;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.config.oAuth2.KakaoConfig;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.global.web.JwtTokenProvider;
import com.ureca.picky_be.jpa.entity.user.Role;
import com.ureca.picky_be.jpa.entity.user.SocialPlatform;
import com.ureca.picky_be.jpa.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class KakaoManager {

    private final KakaoConfig kakaoConfig;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private final LineReviewRepository lineReviewRepository;
    private final LineReviewLikeRepository lineReviewLikeRepository;
    private final LineReviewSoftDeleteRepository lineReviewSoftDeleteRepository;
    private final MovieLikeRepository movieLikeRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final UserGenrePreferenceRepository userGenrePreferenceRepository;

    RestClient restClient = RestClient.create();

    public LoginUrlResp buildCodeUrl(String state){
        return new LoginUrlResp(UriComponentsBuilder
                .fromHttpUrl(kakaoConfig.getCodeUrl())
                .queryParam("client_id", kakaoConfig.getClientId())
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", kakaoConfig.getRedirectUri())
                .queryParam("state", state)
                .build()
                .toUriString());
    }

    public OAuth2Token getOAuth2Token(String code){
        try{
            return restClient
                    .post()
                    .uri(buildTokenUrl(code))
                    .retrieve()
                    .toEntity(OAuth2Token.class)
                    .getBody();
        } catch (RestClientResponseException e) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String buildTokenUrl(String code){
        return UriComponentsBuilder
                .fromHttpUrl(kakaoConfig.getTokenUrl())
                .queryParam("client_id", kakaoConfig.getClientId())
                .queryParam("client_secret", kakaoConfig.getClientSecret())
                .queryParam("code", code)
                .queryParam("grant_type", "authorization_code")
                .queryParam("redirect_uri", kakaoConfig.getRedirectUri())
                .build()
                .toUriString();
    }

    public String getUserInfo(String accessToken){
        try {
            Map response = restClient
                    .get()
                    .uri(buildInfoUrl())
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .retrieve()
                    .body(Map.class);
            return (String) ((Map) response.get("kakao_account")).get("email");

        } catch (RestClientResponseException e) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String buildInfoUrl(){
        return UriComponentsBuilder
                .fromHttpUrl(kakaoConfig.getInfoUrl())
                .build()
                .toUriString();
    }

    public LoginUserInfo getLocalJwt(String email, String accessToken) {
        try{
            User user = userRepository.findByEmailAndSocialPlatform(email, SocialPlatform.KAKAO)
                    .orElseGet(() -> createNewUser(email));
            return new LoginUserInfo(
                    jwtTokenProvider.generate(user.getId(), user.getRole().toString()),
                    user.getId(),
                    user.getRole().toString()
            );
        } catch (Exception e){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    public boolean isRegistrationDone(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.getName() == null
                || user.getNickname() == null
                || user.getBirthdate() == null
                || user.getGender() == null
                || user.getNationality() == null) {
            return false;
        }
        return true;
    }

    private User createNewUser(String email) {
        try{
            User newUser = User.builder()
                    .socialPlatform(SocialPlatform.KAKAO)
                    .role(Role.USER)
                    .email(email)
                    .build();
            return userRepository.save(newUser);
        } catch (Exception e){
            throw new CustomException(ErrorCode.USER_SAVE_FAILED);
        }
    }

    @Transactional
    public SuccessCode deleteAccount(Long userId, DeleteUserReq req) {
        restClient
                .post()
                .uri(buildDeleteUrl(req.oAuth2Token().accessToken()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + req.oAuth2Token().accessToken())
                .retrieve()
                .toBodilessEntity();


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 연관된 자료 삭제
        lineReviewLikeRepository.deleteByUserId(userId);
        lineReviewRepository.deleteByUserId(userId);
        lineReviewSoftDeleteRepository.deleteByUserId(userId);

        movieLikeRepository.deleteByUserId(userId);

        boardLikeRepository.deleteByUserId(userId);
        boardCommentRepository.deleteByUserId(userId);
        boardRepository.deleteByUserId(userId);

        userGenrePreferenceRepository.deleteByUserId(userId);

        // 유저 삭제
        userRepository.deleteById(userId);
        return SuccessCode.REQUEST_DELETE_ACCOUNT_SUCCESS;
    }

    private String buildDeleteUrl(String accessToken){
        return UriComponentsBuilder
                .fromHttpUrl(kakaoConfig.getDeleteUrl())
                .queryParam("token", accessToken)
                .build()
                .toUriString();
    }
}
