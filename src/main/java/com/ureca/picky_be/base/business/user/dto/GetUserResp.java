package com.ureca.picky_be.base.business.user.dto;

import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.user.Gender;
import com.ureca.picky_be.jpa.user.Nationality;

import java.time.LocalDate;
import java.util.List;

public record GetUserResp(String name,
                          String nickname,
                          String profile_url,
                          LocalDate birthdate,
                          Gender gender,
                          Nationality nationality,
                          List<Genre> genres) {}
