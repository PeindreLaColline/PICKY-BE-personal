package com.ureca.picky_be.base.implementation.playlist;

import com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieProjection;
import com.ureca.picky_be.base.business.playlist.dto.AddPlaylistReq;
import com.ureca.picky_be.base.business.playlist.dto.GetPlaylistProjection;
import com.ureca.picky_be.base.persistence.movie.MovieRepository;
import com.ureca.picky_be.base.persistence.playlist.MoviePlaylistRepository;
import com.ureca.picky_be.base.persistence.playlist.PlaylistRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.movie.Movie;
import com.ureca.picky_be.jpa.movie.MoviePlayList;
import com.ureca.picky_be.jpa.playlist.Playlist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlaylistManager {

    private final PlaylistRepository playlistRepository;
    private final MovieRepository movieRepository;
    private final MoviePlaylistRepository moviePlaylistRepository;

    public Slice<GetPlaylistProjection> getPlaylistProjections(Long lastPlaylistId, int size) {
        return playlistRepository.getPlaylistsNative(lastPlaylistId, PageRequest.ofSize(size));
    }

    public List<GetSimpleMovieProjection> getSimpleMovieProjections(Slice<GetPlaylistProjection> playlistProjections) {
        List<Long> playlistIds = playlistProjections.stream()
                .map(GetPlaylistProjection::getPlaylistId)
                .toList();
        return movieRepository.getMoviesByPlaylistIds(playlistIds);
    }

    public Playlist addPlaylist(AddPlaylistReq addPlaylistReq) {
        Playlist playlist = Playlist.builder()
                .title(addPlaylistReq.title())
                .build();
        playlistRepository.save(playlist);
        addPlaylistReq.movieIds().stream()
                .map(movieId -> {
                    Movie movie = movieRepository.findById(movieId)
                            .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

                    return MoviePlayList.builder()
                            .playlist(playlist)
                            .movieId(movie)
                            .build();
                })
                .forEach(moviePlaylistRepository::save);
        return playlist;
    }
}
