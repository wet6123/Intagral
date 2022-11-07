package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByContent(String content);

    List<Hashtag> findAllByContentContaining(String target);

    List<Hashtag> findTop5ByOrderBySearchCntDesc();
}