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
public class Hashtag extends BaseEntity {

    @Id
    @Column(name = "hashtag_id", columnDefinition = "INT UNSIGNED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String content;
    @Column(nullable = false)
    Integer searchCnt;

    @OneToMany(mappedBy = "hashtagFollowHashtag", fetch = FetchType.LAZY)
    List<HashtagFollow> hashtagFollowList = new ArrayList<>();

    @OneToMany(mappedBy = "hashtagFollowPostHashtag", fetch = FetchType.LAZY)
    List<PostHashtag> hashtagPostList = new ArrayList<>();
}
