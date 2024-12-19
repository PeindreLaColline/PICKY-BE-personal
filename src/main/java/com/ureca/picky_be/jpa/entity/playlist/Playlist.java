package com.ureca.picky_be.jpa.entity.playlist;

import com.ureca.picky_be.jpa.entity.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Playlist extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    public void updatePlaylistTitle(String title){
        this.title = title;
    }
}
