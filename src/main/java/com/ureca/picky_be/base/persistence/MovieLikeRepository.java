package com.ureca.picky_be.base.persistence;

import com.ureca.picky_be.jpa.movie.MovieLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {
    void deleteByUserId(Long userId);
}
