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
        columnNames = {"user_id_from", "user_id_to"})
)
public class UserFollow extends BaseEntity {

    @Id
    @Column(name = "user_follow_id", columnDefinition = "INT UNSIGNED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id_from",nullable = false)
    Integer userIdFrom;
    @Column(name = "user_id_to", nullable = false)
    Integer userIdTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_from", insertable = false, updatable = false)
    User userFollowingToUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_to", insertable = false, updatable = false)
    User userFollowerToUser;
}