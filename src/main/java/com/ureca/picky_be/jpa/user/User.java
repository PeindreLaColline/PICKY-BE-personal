package com.ureca.picky_be.jpa.user;

import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="is_active", nullable = false)
    @ColumnDefault("true")
    private boolean isActive;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private String profileUrl;

    @Column(nullable = false)
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialPlatform socialPlatform;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Nationality nationality;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

}
