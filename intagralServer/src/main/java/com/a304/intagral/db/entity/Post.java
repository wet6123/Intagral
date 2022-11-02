package com.a304.intagral.db.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity  {

    @Id
    @Column(name = "post_id", columnDefinition = "INT UNSIGNED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long postId = null;

    @Column(nullable = false, unique = true)
    Long userId = null;

    @Column(nullable = false)
    LocalDateTime postDate;

    String imagePath;

    @Column(nullable = false)
    Integer likeCnt;
}
