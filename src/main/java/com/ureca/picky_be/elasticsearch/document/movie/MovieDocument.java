package com.ureca.picky_be.elasticsearch.document.movie;

import com.ureca.picky_be.jpa.entity.config.IsDeleted;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

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

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Date)
    private Date releaseDate;

    @Field(type = FieldType.Keyword)
    private String posterUrl;

    @Field(type = FieldType.Boolean)
    private IsDeleted isDeleted;

}
