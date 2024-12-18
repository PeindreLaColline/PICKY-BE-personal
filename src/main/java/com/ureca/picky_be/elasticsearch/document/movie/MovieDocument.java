package com.ureca.picky_be.elasticsearch.document.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ureca.picky_be.base.business.movie.dto.GetGenres;
import com.ureca.picky_be.jpa.entity.config.IsDeleted;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
@Document(indexName = "connector-movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDocument {
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "mixed_ngram_analyzer", searchAnalyzer = "nori_mixed_analyzer")
    private String title;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    @JsonProperty("release_date") // Elasticsearch 필드 이름과 일치
    private Date releaseDate;

    @Field(type = FieldType.Text)
    @JsonProperty("poster_url") // Elasticsearch 필드 이름과 일치
    private String posterUrl;

    @Field(type = FieldType.Text)
    @JsonProperty("original_language") // Elasticsearch 필드 이름과 일치
    private String originalLanguage;

    @Field(type = FieldType.Nested)
    private List<GetGenres> genre;

    @Field(type = FieldType.Text)
    @JsonProperty("is_deleted") // Elasticsearch 필드 이름과 일치
    private IsDeleted isDeleted;
}
