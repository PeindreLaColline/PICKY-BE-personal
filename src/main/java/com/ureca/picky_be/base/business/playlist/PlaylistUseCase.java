package com.ureca.picky_be.base.business.playlist;

import com.ureca.picky_be.base.business.playlist.dto.AddPlaylistReq;
import com.ureca.picky_be.base.business.playlist.dto.AddPlaylistResp;
import com.ureca.picky_be.base.business.playlist.dto.GetPlaylistResp;
import org.springframework.data.domain.Slice;

public interface PlaylistUseCase {
    Slice<GetPlaylistResp> getPlaylist(Long lastPlaylistId, Integer size);
    AddPlaylistResp addPlaylist(AddPlaylistReq addPlaylistReq);
}
