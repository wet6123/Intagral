package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    Long countByUserIdTo(Integer targetUserId);

    Long countByUserIdFrom(Integer targetUserId);

    Long countByUserIdToAndUserIdFrom(int intValue, Integer targetUserId);
}