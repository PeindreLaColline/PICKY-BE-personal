package com.ureca.picky_be.base.persistence.movieworker;

import com.ureca.picky_be.jpa.entity.movieworker.MovieWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieWorkerRepository extends JpaRepository<MovieWorker, Integer> {
    Optional<MovieWorker> findById(Long id);
}