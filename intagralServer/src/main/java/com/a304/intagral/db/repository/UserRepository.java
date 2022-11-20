package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long userId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM user WHERE nickname LIKE CONCAT('%', :target, '%') ORDER BY CASE WHEN nickname = :target THEN 0 WHEN nickname LIKE CONCAT(:target, '%') THEN 1 WHEN nickname LIKE CONCAT('%', :target, '%') THEN 2 WHEN nickname LIKE CONCAT('%', :target) THEN 3 ELSE 4 END", nativeQuery = true)
    List<User> findAllByNicknameContaining(@Param(value = "target") String target);

    List<User> findByIdIn(List<Long> userIdList);

    Integer countByNickname(String nickname);
}
