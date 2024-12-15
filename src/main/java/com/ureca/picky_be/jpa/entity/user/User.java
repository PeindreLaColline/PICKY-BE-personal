package com.ureca.picky_be.jpa.entity.user;

import com.ureca.picky_be.base.business.user.dto.RegisterUserReq;
import com.ureca.picky_be.jpa.entity.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

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

    //@Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name="is_active", nullable = false)
    @ColumnDefault("true")
    private boolean isActive;           // 활성 사용자, 비활성 사용자

    //@Column(nullable = false)
    @Column(unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    //@Column(nullable = false)
    private Gender gender;

    private String profileUrl;

    //@Column(nullable = false)
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialPlatform socialPlatform;

    @Enumerated(EnumType.STRING)
    //@Column(nullable = false)
    private Nationality nationality;

    @Enumerated(EnumType.STRING)
    //@Column(nullable = false)
    private Status status;              // 정지된 사용자, 일반 사용자

    public void registerUser(RegisterUserReq req) {
        this.name = req.name();
        this.nickname = req.nickname();
        this.birthdate = req.birthdate();
        this.gender = req.gender();
        this.nationality = req.nationality();
        this.status = Status.REGULAR;
        this.isActive = true;
    }

    public void registerProfile(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
