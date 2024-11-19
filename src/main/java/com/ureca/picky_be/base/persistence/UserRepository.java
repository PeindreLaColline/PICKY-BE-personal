package com.ureca.picky_be.base.persistence;

import com.ureca.picky_be.jpa.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
