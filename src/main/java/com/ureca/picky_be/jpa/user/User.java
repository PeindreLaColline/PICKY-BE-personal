package com.ureca.picky_be.jpa.user;

import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isActive;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profileUrl;

    private Date birthdate;

    @Enumerated(EnumType.STRING)
    private SocialPlatform socialPlatform;

    @Enumerated(EnumType.STRING)
    private Nationality nationality;

}
