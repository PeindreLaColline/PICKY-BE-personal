package com.ureca.picky_be.base.implementation.user;

import com.ureca.picky_be.base.business.user.dto.UpdateUserReq;
import com.ureca.picky_be.base.persistence.GenreRepository;
import com.ureca.picky_be.base.persistence.MovieGenreRepository;
import com.ureca.picky_be.base.persistence.UserGenrePreferenceRepository;
import com.ureca.picky_be.base.persistence.UserRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.user.User;
import com.ureca.picky_be.jpa.user.UserGenrePreference;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserManager {
    private final UserRepository userRepository;
    private final UserGenrePreferenceRepository userGenrePreferenceRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreRepository movieGenreRepository;

    @Transactional
    public SuccessCode updateUserInfo(UpdateUserReq req) {
        validateUpdateUserReq(req);
        User user = userRepository.findById(
                Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName())
        ).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.updateUser(req);
        if (req.movieId() != null && !req.movieId().isEmpty()) {
            updateUserGenrePreference(user, req.movieId());
        }
        return SuccessCode.UPDATE_USER_SUCCESS;
    }

    private void validateUpdateUserReq(UpdateUserReq req) {
        if (req.name() == null || req.nickname() == null || req.profile_url() == null ||
                req.birthdate() == null || req.gender() == null || req.nationality() == null) {
            throw new CustomException(ErrorCode.USER_UPDATE_FAILED);
        }
    }

    private void updateUserGenrePreference(User user, List<Long> movieIds) {
        if (movieIds == null || movieIds.isEmpty()) {
            return;
        }

        Map<Long, Integer> genreCountMap = new HashMap<>();
        for (Long movieId : movieIds) {
            List<Long> genreIds = movieGenreRepository.getGenreIdsByMovieId(movieId);
            if (genreIds != null) {
                for (Long genreId : genreIds) {
                    genreCountMap.put(genreId, genreCountMap.getOrDefault(genreId, 0) + 1);
                }
            }
        }

        List<Map.Entry<Long, Integer>> topGenres = genreCountMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .toList();

        for (Map.Entry<Long, Integer> entry : topGenres) {
            UserGenrePreference preference = UserGenrePreference.builder()
                    .user(user)
                    .genreId(entry.getKey())
                    .preferenceValue(entry.getValue().doubleValue())
                    .build();
            userGenrePreferenceRepository.save(preference);
        }
    }

    @Transactional(readOnly = true)
    public User getUserInfo(){
        return userRepository.findById(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()))
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
