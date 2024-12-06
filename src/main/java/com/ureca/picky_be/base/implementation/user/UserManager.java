package com.ureca.picky_be.base.implementation.user;

import com.ureca.picky_be.base.business.user.dto.GetNicknameValidationResp;
import com.ureca.picky_be.base.business.user.dto.UpdateUserReq;
import com.ureca.picky_be.base.persistence.movie.GenreRepository;
import com.ureca.picky_be.base.persistence.movie.MovieGenreRepository;
import com.ureca.picky_be.base.persistence.movie.MovieLikeRepository;
import com.ureca.picky_be.base.persistence.movie.MovieRepository;
import com.ureca.picky_be.base.persistence.user.UserGenrePreferenceRepository;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.movie.MovieLike;
import com.ureca.picky_be.jpa.user.User;
import com.ureca.picky_be.jpa.user.UserGenrePreference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserManager {
    private final UserRepository userRepository;
    private final UserGenrePreferenceRepository userGenrePreferenceRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieLikeRepository movieLikeRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public SuccessCode updateUserInfo(Long userId, UpdateUserReq req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        validateUpdateUserReq(req);
        if(!isRegistrationDone(userId)){
            registerUserGenrePreference(user, req.genreId());
            registerUserMovieLike(user, req.movieId());
            if (req.movieId().isEmpty()) {
                throw new CustomException(ErrorCode.USER_UPDATE_BAD_REQUEST);
            }
        }
        user.updateUser(req);
        return SuccessCode.UPDATE_USER_SUCCESS;
    }

    private void validateUpdateUserReq(UpdateUserReq req) {
        if (req.name() == null
                || req.nickname() == null
                || req.birthdate() == null
                || req.gender() == null
                || req.nationality() == null) {
            throw new CustomException(ErrorCode.USER_UPDATE_FAILED);
        }
    }

    public boolean isRegistrationDone(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getName() != null
                && user.getNickname() != null
                && user.getBirthdate() != null
                && user.getGender() != null
                && user.getNationality() != null;
    }

    // 선택한 장르들을 단순히 저장
    @Transactional
    protected void registerUserGenrePreference(User user, List<Long> genreIds) {
        List<UserGenrePreference> preferences = genreIds.stream()
                .map(genreId -> UserGenrePreference.builder()
                        .user(user)
                        .genreId(genreId)
                        .preferenceValue(1.0)
                        .build())
                .toList();
        userGenrePreferenceRepository.saveAll(preferences);
    }

    // 선택한 영화 좋아요
    @Transactional
    protected void registerUserMovieLike(User user, List<Long> movieIds) {
        List<MovieLike> movieLikes = movieIds.stream()
                .map(movieId -> MovieLike.builder()
                        .user(user)
                        .movie(movieRepository.findById(movieId).get())
                        .build())
                .toList();
        movieLikeRepository.saveAll(movieLikes);
    }

    // 선택한 영화 기반으로 선호 장르 추출 후 저장
    /*@Transactional
    protected void updateUserGenrePreference(User user, List<Long> movieIds) {
        Map<Long, Integer> genreCountMap = movieIds.stream()
                .map(movieGenreRepository::getGenreIdsByMovieId)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toMap(
                        genreId -> genreId,
                        genreId -> 1,
                        Integer::sum
                ));

        List<Map.Entry<Long, Integer>> topGenres = genreCountMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .toList();

        List<UserGenrePreference> preferences = topGenres.stream()
                .map(entry -> UserGenrePreference.builder()
                        .user(user)
                        .genreId(entry.getKey())
                        .preferenceValue(entry.getValue().doubleValue())
                        .build())
                .toList();

        userGenrePreferenceRepository.deleteByUserId(user.getId());
        userGenrePreferenceRepository.saveAll(preferences);
    }*/

    @Transactional(readOnly = true)
    public User getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Genre> getUserGenrePreference(Long userId){
        List<UserGenrePreference> preferences = userGenrePreferenceRepository.findByUserId(userId);

        return preferences.stream()
                .map(preference -> genreRepository.findById(preference.getGenreId())
                        .orElseThrow(() -> new CustomException(ErrorCode.GENRE_NOT_FOUND)))
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean getNicknameValidation(String nickname){
        return !userRepository.existsByNickname(nickname);
    }
}
