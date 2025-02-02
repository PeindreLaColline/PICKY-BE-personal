package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.entity.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findById(Long id);
    List<Genre> findAllByIdIn(List<Long> genreIds);
}
