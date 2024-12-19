package com.ureca.picky_be.base.business.user.dto;

import com.ureca.picky_be.jpa.entity.user.Gender;
import com.ureca.picky_be.jpa.entity.user.Nationality;

import java.time.LocalDate;
import java.util.List;

public record RegisterUserReq(String name,
                              String nickname,
                              LocalDate birthdate,
                              Gender gender,
                              Nationality nationality,
                              List<Long> movieId,
                              List<Long> genreId){
}
