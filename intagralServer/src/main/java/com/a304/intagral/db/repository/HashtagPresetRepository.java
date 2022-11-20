package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.HashtagPreset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HashtagPresetRepository extends JpaRepository<HashtagPreset, Long> {
    @Transactional
    void deleteByUserIdAndHashtagIdAndClsTargetId(int userId, int hashtagId, int clsTargetId);
}