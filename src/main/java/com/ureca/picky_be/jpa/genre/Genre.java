package com.ureca.picky_be.jpa.genre;

import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Genre extends BaseEntity {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

}
