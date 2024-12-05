package com.ureca.picky_be.base.persistence.user;

import com.ureca.picky_be.jpa.user.SocialPlatform;
import com.ureca.picky_be.jpa.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndSocialPlatform(String email, SocialPlatform socialPlatform);
    boolean existsByNickname(String nickname);
}
