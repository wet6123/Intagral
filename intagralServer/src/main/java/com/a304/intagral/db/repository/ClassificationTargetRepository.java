package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.ClassificationTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassificationTargetRepository extends JpaRepository<ClassificationTarget, Long> {

    Optional<ClassificationTarget> findByTargetName(String cls);

    List<ClassificationTarget> findByTargetNameIn(List<String> classList);
}