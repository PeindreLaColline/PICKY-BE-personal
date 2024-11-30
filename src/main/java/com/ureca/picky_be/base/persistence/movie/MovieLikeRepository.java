package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.movie.MovieLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
}
