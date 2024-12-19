package com.ureca.picky_be.base.implementation.user;

import com.ureca.picky_be.base.business.user.dto.RegisterUserReq;
import com.ureca.picky_be.base.business.user.dto.UserInfoProjection;
import com.ureca.picky_be.base.implementation.content.ProfileManager;
import com.ureca.picky_be.base.persistence.follow.FollowRepository;
import com.ureca.picky_be.base.persistence.movie.GenreRepository;
import com.ureca.picky_be.base.persistence.movie.MovieLikeRepository;
import com.ureca.picky_be.base.persistence.movie.MovieRepository;
import com.ureca.picky_be.base.persistence.user.UserGenrePreferenceRepository;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.base.persistence.user.UserSearchRepository;
import com.ureca.picky_be.elasticsearch.document.user.UserDocument;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.genre.Genre;
import com.ureca.picky_be.jpa.entity.movie.MovieLike;
import com.ureca.picky_be.jpa.entity.user.Status;
import com.ureca.picky_be.jpa.entity.user.User;
import com.ureca.picky_be.jpa.entity.user.UserGenrePreference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserManager {
    private final UserRepository userRepository;
    private final UserGenrePreferenceRepository userGenrePreferenceRepository;
    private final GenreRepository genreRepository;
    private final MovieLikeRepository movieLikeRepository;
    private final MovieRepository movieRepository;
    private final FollowRepository followRepository;
    private final ProfileManager profileManager;
    private final UserSearchRepository userSearchRepository;


    @Transactional
    public SuccessCode registerProfile(MultipartFile profile, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(profile.isEmpty()){
            try{
                user.registerProfile(null);
                userRepository.save(user);
            } catch(Exception e){
                throw new CustomException(ErrorCode.USER_UPDATE_FAILED);
            }
        }
        else{
            user.registerProfile(profileManager.uploadProfile(profile));
            userRepository.save(user);
        }
        return SuccessCode.UPDATE_USER_PROFILE_SUCCESS;
    }

    @Transactional
    public User registerUserInfo(Long userId, RegisterUserReq req) {
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
        user.registerUser(req);
        return user;
    }

    @Transactional
    public SuccessCode updateUserNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        try {
            user.updateNickname(nickname);
            return SuccessCode.UPDATE_USER_SUCCESS;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_NICKNAME);
        }
    }

    private void updateElasticsearchUserNickname(Long userId, String nickname) {
        UserDocument userDocument = userSearchRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userDocument.setNickname(nickname);
        userSearchRepository.save(userDocument);
    }

    private void validateUpdateUserReq(RegisterUserReq req) {
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

    @Transactional(readOnly = true)
    public Long getUserIdByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public UserInfoProjection getUserInfoById(Long userId) {
        return userRepository.findUserInfoById(userId);
    }

    @Transactional(readOnly = true)
    public Integer getUserFollowerCount(Long userId) {
        return followRepository.countByFollowerId(userId);
    }

    @Transactional(readOnly = true)
    public Integer getUserFollowingCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }

    @Transactional(readOnly = true)
    public String getUserEmailById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(user.getEmail() == null || user.getEmail().isEmpty())
            throw new CustomException(ErrorCode.USER_EMAIL_EMPTY);
        return user.getEmail();
    }

    public void validateUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(user.getStatus() != Status.REGULAR){
            throw new CustomException(ErrorCode.USER_SUSPENDED);
        }
    }

    @Transactional(readOnly = true)
    public List<UserDocument> getSearchUsers(String keyword) {
        return userSearchRepository.findByNicknameExcludingAdminAndSuspended(keyword);
    }

    public void addUserElastic(User user) {
        try{
            UserDocument newUserDocument = UserDocument.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .role(user.getRole())
                    .status(user.getStatus())
                    .build();
            userSearchRepository.save(newUserDocument);
            System.out.println(user.getNickname());
        }catch (Exception e){
            throw new CustomException(ErrorCode.ELASTIC_USER_CREATE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getEmail() == null || user.getEmail().isEmpty())
            throw new CustomException(ErrorCode.USER_EMAIL_EMPTY);
        return user;
    }
}
