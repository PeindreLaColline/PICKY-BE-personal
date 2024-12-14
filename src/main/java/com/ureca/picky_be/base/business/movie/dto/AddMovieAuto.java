package com.ureca.picky_be.base.business.movie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public record AddMovieAuto(
        @JsonProperty("id") Long id,
        @JsonProperty("title") String title,
        @JsonProperty("release_date") Date releaseDate,
        @JsonProperty("poster_path") String posterUrl,
        @JsonProperty("overview") String plot,
        @JsonProperty("runtime") int runtime,
        @JsonProperty("genres") List<AddMovieReq.MovieInfo.GenreInfo> genres,
        @JsonProperty("credits") AddMovieReq.MovieInfo.Credits credits,
        @JsonProperty("original_language") String originalLanguage,
        @JsonProperty("popularity") double popularity,
        @JsonProperty("backdrop_path") String backdropUrl
){
    public record GenreInfo(
            @JsonProperty("id") Long id
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Credits(
            List<Cast> cast,
            List<Crew> crew
    ) {
        public record Cast(
                @JsonProperty("id") Long id,
                @JsonProperty("character") String role,
                @JsonProperty("original_name") String name,
                @JsonProperty("profile_path") String profileUrl
        ) {}

        public record Crew(
                @JsonProperty("id") Long id,
                @JsonProperty("job") String job,
                @JsonProperty("original_name") String name,
                @JsonProperty("profile_path") String profileUrl
        ) {}

        public List<Crew> getDirectingCrew() {
            return crew.stream()
                    .filter(c -> "Director".equals(c.job))
                    .collect(Collectors.toList());
        }
    }
}