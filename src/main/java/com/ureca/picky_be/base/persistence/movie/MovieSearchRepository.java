package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.elasticsearch.document.movie.MovieDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface MovieSearchRepository extends ElasticsearchRepository <MovieDocument, Long> {
    @Query("""
        {
          "match": {
            "title": {
              "query": "?0",
              "analyzer": "mixed_ngram_analyzer"
            }
          }
        }
        """)
    List<MovieDocument> findByTitle(String title);



}
