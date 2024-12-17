package com.ureca.picky_be.elasticsearch.document.user;

import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.entity.user.Role;
import com.ureca.picky_be.jpa.entity.user.Status;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "connector-user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDocument {
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String email;

    @Field(type = FieldType.Text, analyzer = "autocomplete_analyzer", searchAnalyzer = "mixed_analyzer")
    private String nickname;

    @Field(type = FieldType.Keyword)
    private Role role = Role.USER;

    @Field(type = FieldType.Keyword)
    private Status status;

    public void setId(String rawId) {
        if (rawId != null && rawId.matches("user_\\d+")) {
            this.id = Long.parseLong(rawId.split("_")[1]);
        } else {
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }
    }
}
