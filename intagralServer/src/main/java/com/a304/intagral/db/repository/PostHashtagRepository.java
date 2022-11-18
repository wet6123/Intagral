package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.PostHashtag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    List<PostHashtag> findByHashtagId(Integer hashtagId, Sort sort);

    List<PostHashtag> findByHashtagIdIn(List<Integer> hashtagIdList, Sort id);
}