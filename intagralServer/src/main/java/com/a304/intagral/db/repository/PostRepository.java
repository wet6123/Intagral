package com.a304.intagral.db.repository;

import com.a304.intagral.db.entity.Post;
import com.a304.intagral.dto.PostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByPostIdEquals(Long postId);




    //JPQL하다가 화남

//  전체 최신 게시물 목록
//    @Query("select p, from Post as p, where p.")
//    List<Object[]> getNewestPostList(int page);


//  해시태그 기반 게시물 목록
//    @Query("select p, from Post p, where p.hashtagId = ");
//    List<Object[]> getNewestPostList(int tag);

//  개인 페이지 안 게시물 목록

//  팔로우 기반 게시물 목록

//  게시물 detail
}
