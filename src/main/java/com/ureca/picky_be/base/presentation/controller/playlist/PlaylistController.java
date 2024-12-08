package com.ureca.picky_be.base.presentation.controller.playlist;

import com.ureca.picky_be.base.business.playlist.PlaylistUseCase;
import com.ureca.picky_be.base.business.playlist.dto.GetPlaylistResp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/playlist")
public class PlaylistController {

    private final PlaylistUseCase playlistUseCase;

    @GetMapping("/all")
    public Slice<GetPlaylistResp> getPlaylist(
            @RequestParam(value = "last-playlist-id", required = false) Long lastPlaylistId,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size){
        return playlistUseCase.getPlaylist(lastPlaylistId, size);
    }
}
