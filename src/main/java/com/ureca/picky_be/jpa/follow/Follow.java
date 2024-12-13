package com.ureca.picky_be.jpa.follow;

import com.ureca.picky_be.jpa.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "follower_id", nullable = false)
    private Long followerId;        // 신청자(상대한테 신청하는 사람)

    @Column(name = "following_id", nullable = false)
    private Long followingId;       // 수령자(누군가의 신청을 받는 사람)


}
