package com.a304.intagral.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false, unique = true)
    String nickname;

    String profileImgPath;
    String intro;
    String accessToken;
    String refreshToken;
    String authToken;
    String oauthPlatform;

    @OneToMany(mappedBy = "userFollowingToUser", fetch = FetchType.LAZY)
    List<UserFollow> userFollowingList = new ArrayList<>();

    @OneToMany(mappedBy = "userFollowerToUser", fetch = FetchType.LAZY)
    List<UserFollow> userFollowerList = new ArrayList<>();

    @OneToMany(mappedBy = "postToUser", fetch = FetchType.LAZY)
    List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "hashtagFollowToUser", fetch = FetchType.LAZY)
    List<HashtagFollow> hashtagFollowList = new ArrayList<>();

    @OneToMany(mappedBy = "postLikeToUser", fetch = FetchType.LAZY)
    List<PostLike> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "hashtagPresetToUser", fetch = FetchType.LAZY)
    List<HashtagPreset> hashtagPresetList = new ArrayList<>();
}