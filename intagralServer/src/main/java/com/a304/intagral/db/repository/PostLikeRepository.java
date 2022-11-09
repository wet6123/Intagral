package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Integer countByUserIdAndPostId(Long userId, Long postId);

    @Transactional
    void deleteByUserIdAndPostId(Long userId, Long postId);
}