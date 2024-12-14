package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.recommend.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Integer> {
    @Query("""
    SELECT r.movieId FROM Recommend r WHERE r.userId = :userId
""")
    List<Long> findAllMovieIdsByUserId(Long userId);
}
