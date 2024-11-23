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

    private String userEmail;

    private String userName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean is_active;

    private String userNickname;

    @Enumerated(EnumType.STRING)
    private Gender userGender;

    private String userProfileUrl;

    private Date userBirthdate;

    @Enumerated(EnumType.STRING)
    private SocialPlatform userSocialPlatform;

    @Enumerated(EnumType.STRING)
    private Nationality userNationality;

}
