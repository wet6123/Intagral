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
        columnNames = {"user_id", "hashtag_id"})
)
public class HashtagFollow extends BaseEntity {

    @Id
    @Column(name = "hashtag_follow_id", columnDefinition = "INT UNSIGNED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = " user_id", nullable = false)
    Integer userId;
    @Column(name = "hashtag_id", nullable = false)
    Integer hashtagId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    User hashtagFollowToUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", nullable = false, insertable = false, updatable = false)
    Hashtag hashtagFollowToHashtag;
}