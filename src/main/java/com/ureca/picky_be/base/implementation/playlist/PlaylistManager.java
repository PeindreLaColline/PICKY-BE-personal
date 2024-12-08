package com.ureca.picky_be.base.implementation.playlist;

import com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieProjection;
import com.ureca.picky_be.base.business.playlist.dto.GetPlaylistProjection;
import com.ureca.picky_be.base.persistence.movie.MovieRepository;
import com.ureca.picky_be.base.persistence.playlist.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaylistManager {

    private final PlaylistRepository playlistRepository;
    private final MovieRepository movieRepository;

    public Slice<GetPlaylistProjection> getPlaylistProjections(Long lastPlaylistId, int size) {
        return playlistRepository.getPlaylistsNative(lastPlaylistId, PageRequest.ofSize(size));
    }

    public List<GetSimpleMovieProjection> getSimpleMovieProjections(Slice<GetPlaylistProjection> playlistProjections) {
        List<Long> playlistIds = playlistProjections.stream()
                .map(GetPlaylistProjection::getPlaylistId)
                .toList();
        System.out.println(playlistIds);
        return movieRepository.getMoviesByPlaylistIds(playlistIds);
    }
}
