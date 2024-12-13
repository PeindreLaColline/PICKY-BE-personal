package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.platform.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
