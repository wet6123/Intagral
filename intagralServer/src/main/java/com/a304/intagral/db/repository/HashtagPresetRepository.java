package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.HashtagPreset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagPresetRepository extends JpaRepository<HashtagPreset, Long> {

}