package com.ureca.picky_be.base.implementation.user;

import com.ureca.picky_be.base.business.user.dto.UpdateUserReq;
import com.ureca.picky_be.base.persistence.movie.GenreRepository;
import com.ureca.picky_be.base.persistence.movie.MovieGenreRepository;
import com.ureca.picky_be.base.persistence.user.UserGenrePreferenceRepository;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.genre.Genre;
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

    @Transactional
    public SuccessCode updateUserInfo(Long userId, UpdateUserReq req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        validateUpdateUserReq(req);
        user.updateUser(req);
        if (req.movieId() == null || req.movieId().isEmpty()) {
            throw new CustomException(ErrorCode.USER_UPDATE_BAD_REQUEST);
        }
        updateUserGenrePreference(user, req.movieId());
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

    @Transactional
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
    }

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
}
