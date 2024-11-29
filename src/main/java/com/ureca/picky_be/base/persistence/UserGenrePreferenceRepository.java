package com.ureca.picky_be.base.persistence;

import com.ureca.picky_be.jpa.user.UserGenrePreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGenrePreferenceRepository extends JpaRepository<UserGenrePreference, Long> {
    List<UserGenrePreference> findByUserId(Long userId);
    void deleteByUserId(@Param("userId") Long userId);
}
