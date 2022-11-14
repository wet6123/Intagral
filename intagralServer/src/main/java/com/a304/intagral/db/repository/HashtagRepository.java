package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByContent(String content);

    @Query(value = "SELECT * FROM hashtag WHERE content LIKE CONCAT('%', :target, '%') ORDER BY CASE WHEN content = :target THEN 0 WHEN content LIKE CONCAT(:target, '%') THEN 1 WHEN content LIKE CONCAT('%', :target, '%') THEN 2 WHEN content `LIKE CONCAT('%', :target) THEN 3 ELSE 4 END", nativeQuery = true)
    List<Hashtag> findAllByContentContaining(String target);

    List<Hashtag> findTop5ByOrderBySearchCntDesc();

    List<Hashtag> findByIdIn(List<Long> hashtagIdList);
}