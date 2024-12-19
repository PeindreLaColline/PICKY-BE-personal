package com.ureca.picky_be.base.persistence.user;

import com.ureca.picky_be.jpa.entity.user.UserGenrePreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserGenrePreferenceRepository extends JpaRepository<UserGenrePreference, Long> {
    List<UserGenrePreference> findByUserId(Long userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);
}
