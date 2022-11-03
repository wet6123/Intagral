package com.a304.intagral.db.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id", columnDefinition = "INT UNSIGNED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    User postUser;

    String imgPath;
    @Column(nullable = false)
    LocalDateTime postDate;
    @Column(nullable = false)
    Integer likeCnt;

    @OneToMany(mappedBy = "postLikePost", fetch = FetchType.LAZY)
    List<PostLike> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "postHashtagUser", fetch = FetchType.LAZY)
    List<PostHashtag> postHashtagList = new ArrayList<>();
}
