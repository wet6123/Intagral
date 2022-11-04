package com.a304.intagral.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationTarget extends BaseEntity {

    @Id
    @Column(name = "cls_target_id", columnDefinition = "INT UNSIGNED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String targetName;

    @OneToMany(mappedBy = "hashtagPresetToClassificationTarget", fetch = FetchType.LAZY)
    List<HashtagPreset> hashtagPresetList = new ArrayList<>();
}
