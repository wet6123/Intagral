package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.HashtagFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HashtagFollowRepository extends JpaRepository<HashtagFollow, Long> {

    Long countByUserIdAndHashtagId(int userId, Integer hashtagId);

    Long countByHashtagId(Integer hashtagId);

    Long countByUserId(Integer targetUserId);
    @Transactional
    void deleteByUserIdAndHashtagId(Integer myUserId, Integer targetHashtagId);
}