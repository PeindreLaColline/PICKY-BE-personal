package com.ureca.picky_be.base.persistence.playlist;

import com.ureca.picky_be.base.business.playlist.dto.GetPlaylistProjection;
import com.ureca.picky_be.jpa.playlist.Playlist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    @Query(value = """
    SELECT pl.id AS playlistId,
           pl.title AS title
    FROM Playlist pl
    WHERE (:lastPlaylistId IS NULL OR pl.id < :lastPlaylistId)
    ORDER BY pl.id DESC
    """)
    Slice<GetPlaylistProjection> getPlaylistsNative(
            @Param("lastPlaylistId") Long lastPlaylistId,
            Pageable pageable
    );

}
