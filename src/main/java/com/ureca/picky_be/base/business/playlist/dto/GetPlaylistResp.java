package com.ureca.picky_be.base.business.playlist.dto;

import com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieResp;

import java.util.List;

public record GetPlaylistResp(
        Long playlistId,
        String title,
        List<GetSimpleMovieResp> getSimpleMovieResps
) {}
