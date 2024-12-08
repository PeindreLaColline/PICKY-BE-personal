package com.ureca.picky_be.base.persistence.playlist;

import com.ureca.picky_be.jpa.movie.MoviePlayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviePlaylistRepository extends JpaRepository<MoviePlayList, Integer> {
}
