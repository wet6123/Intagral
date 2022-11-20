package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    Long countByUserIdTo(Integer targetUserId);

    Long countByUserIdFrom(Integer targetUserId);

    Long countByUserIdToAndUserIdFrom(int intValue, Integer targetUserId);

    @Transactional
    void deleteByUserIdToAndUserIdFrom(Integer myUserId, Integer targetUserId);
}