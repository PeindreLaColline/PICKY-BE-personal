package com.ureca.picky_be.jpa.user;

import com.ureca.picky_be.base.business.user.dto.UpdateUserReq;
import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
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

    //nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name="is_active", nullable = false)
    @ColumnDefault("true")
    private boolean isActive;

    //@Column(nullable = false)
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
    private Status status;

    public void updateUser(UpdateUserReq req) {
        if (req.name() != null) {
            this.name = req.name();
        }
        if (req.nickname() != null) {
            this.nickname = req.nickname();
        }
        if (req.profile_url() != null) {
            this.profileUrl = req.profile_url();
        }
        if (req.birthdate() != null) {
            this.birthdate = req.birthdate();
        }
        if (req.gender() != null) {
            this.gender = req.gender();
        }
        if (req.nationality() != null) {
            this.nationality = req.nationality();
        }
        this.status = Status.REGULAR;
        this.isActive = true;
    }
}
