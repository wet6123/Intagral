package com.a304.intagral.db.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "hashtag_id", "cls_target_id"})
)
public class HashtagPreset extends BaseEntity {

    @Id
    @Column(name = "hashtag_preset_id", columnDefinition = "INT UNSIGNED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    User hashtagPresetToUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", nullable = false, insertable = false, updatable = false)
    Hashtag hashtagPresetToHashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cls_target_id", nullable = false, insertable = false, updatable = false)
    ClassificationTarget hashtagPresetToClassificationTarget;
}