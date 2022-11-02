package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long userId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);
}
