package com.ureca.picky_be.elasticsearch.document.movie;

import com.ureca.picky_be.jpa.entity.config.IsDeleted;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.util.Date;
import java.util.List;

@Document(indexName = "movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDocument {
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "autocomplete_analyzer", searchAnalyzer = "mixed_analyzer")
    private String title;

    @Field(type = FieldType.Date)
    private Date releaseDate;

    @Field(type = FieldType.Keyword)
    private String posterUrl;

    @Field(type = FieldType.Keyword)
    private IsDeleted isDeleted;


}
