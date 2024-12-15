package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.base.business.movie.dto.GetSearchMoviesResp;
import com.ureca.picky_be.elasticsearch.document.movie.MovieDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MovieSearchRepository extends ElasticsearchRepository <MovieDocument, Long> {
    List<MovieDocument> findByTitle(String title);
}
