package com.ureca.picky_be.jpa.following;

import com.ureca.picky_be.jpa.config.BaseEntity;
import com.ureca.picky_be.jpa.user.User;
import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User following;


}
