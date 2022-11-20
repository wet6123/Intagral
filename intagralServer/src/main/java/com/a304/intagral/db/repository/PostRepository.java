package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.Post;
import com.a304.intagral.db.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Integer userId, Sort sort);

    @Transactional
    void deleteById(Long postId);
}