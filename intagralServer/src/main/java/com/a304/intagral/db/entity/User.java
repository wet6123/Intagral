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

    @OneToMany(mappedBy = "followingUser", fetch = FetchType.LAZY)
    List<UserFollow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "followedUser", fetch = FetchType.LAZY)
    List<UserFollow> followerList = new ArrayList<>();

    @OneToMany(mappedBy = "postUser", fetch = FetchType.LAZY)
    List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "hashtagFollowUser", fetch = FetchType.LAZY)
    List<HashtagFollow> hashtagFollowList = new ArrayList<>();
}