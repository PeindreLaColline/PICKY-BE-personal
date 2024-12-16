package com.ureca.picky_be.base.persistence.follow;

import com.ureca.picky_be.jpa.entity.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Integer countByFollowerId(Long userId);
    Integer countByFollowingId(Long userId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long follwerId, Long followingId);

    void deleteFollowByFollowerIdAndFollowingId(Long followerId, Long followingId);



}
