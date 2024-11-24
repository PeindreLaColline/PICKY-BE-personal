package com.ureca.picky_be.jpa.movieworker;

import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class MovieWorker extends BaseEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private String profileUrl;
}
