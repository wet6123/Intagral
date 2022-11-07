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
        columnNames = {"post_id", "hashtag_id"})
)
public class PostHashtag extends BaseEntity {

    @Id
    @Column(name = "post_hashtag_id", columnDefinition = "INT UNSIGNED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, insertable = false, updatable = false)
    Post postHashtagToPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", nullable = false, insertable = false, updatable = false)
    Hashtag postHashtagToHashtag;
}