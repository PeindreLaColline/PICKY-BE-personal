package com.ureca.picky_be.base.persistence.follow;

import com.ureca.picky_be.base.business.follow.dto.FollowProjection;
import com.ureca.picky_be.jpa.entity.follow.Follow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Integer countByFollowerId(Long userId);
    Integer countByFollowingId(Long userId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long follwerId, Long followingId);

    void deleteFollowByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("""
        SELECT f.followerId AS userId,
               u.nickname AS userNickname,
               u.profileUrl AS userProfileUrl,
               u.role AS userRole
       FROM Follow f
       JOIN User u ON f.followerId = u.id
       WHERE f.followingId = :userId
       ORDER BY f.createdAt DESC
    """)
    Slice<FollowProjection> findFollowersByFollowingId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT f.followingId AS userId,
                u.nickname AS userNickname,
                u.profileUrl AS userProfileUrl,
                u.role AS userRole
        FROM Follow f
        JOIN User u ON f.followingId = u.id
        WHERE f.followerId = :userId
        ORDER BY f.createdAt DESC
    """)
    Slice<FollowProjection> findFollowingsByFollowerId(@Param("userId") Long userId, Pageable pageable);
}
