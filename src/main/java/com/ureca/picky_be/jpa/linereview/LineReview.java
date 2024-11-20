package com.ureca.picky_be.jpa.linereview;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class LineReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
