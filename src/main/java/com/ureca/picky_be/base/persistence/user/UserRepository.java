package com.ureca.picky_be.base.persistence.user;

import com.ureca.picky_be.base.business.user.dto.UserInfoProjection;
import com.ureca.picky_be.jpa.entity.user.SocialPlatform;
import com.ureca.picky_be.jpa.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndSocialPlatform(String email, SocialPlatform socialPlatform);
    boolean existsByNickname(String nickname);

    @Query("SELECT u.id AS id FROM User u WHERE u.nickname = :nickname")
    Long findByNickname(@Param("nickname") String nickname);


    @Query("SELECT u.id AS id, u.nickname AS nickname, u.profileUrl AS profileUrl, u.role AS role FROM User u WHERE u.id = :userId")
    UserInfoProjection findUserInfoById(@Param("userId") Long userId);
}
