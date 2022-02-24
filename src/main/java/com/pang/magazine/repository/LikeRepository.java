package com.pang.magazine.repository;

import com.pang.magazine.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    @Query("select count(l) from Likes l where l.post.id = :postID")
    public int countByPostId(@Param("postID") Long postID);

    @Query("select l from Likes l where l.member.id = :userID and l.post.id = :postID")
    public Optional<Likes> findByUserIdAndPostId(@Param("userID") Long userID, @Param("postID") Long postID);

}
