package com.pang.magazine.service;

import com.pang.magazine.domain.Likes;
import com.pang.magazine.domain.Member;
import com.pang.magazine.domain.Post;
import com.pang.magazine.dto.LikesDto;
import com.pang.magazine.exception.exceptionclass.NotAuthenticationException;
import com.pang.magazine.exception.exceptionclass.NotFoundException;
import com.pang.magazine.repository.LikeRepository;
import com.pang.magazine.repository.MemberRepository;
import com.pang.magazine.repository.PostRepository;
import com.pang.magazine.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class LikesService {
    // 좋아요 기능
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity createLikes(Long postId, UserDetailsImpl userDetails) throws AuthenticationException {
        Optional<Member> byUsername = Optional.ofNullable(memberRepository.findByUsername(userDetails.getUsername()));
        if(!byUsername.isPresent()){
            throw new NotAuthenticationException("권한이 없는 유저");
        }

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 게시물"));

        Optional<Likes> likes = likeRepository.findByMemberAndPost(userDetails.getMember(), post);



        if(likes.isPresent()){
            // likes를 찾으면 하트가 되어있기 대문에 취소를 시켜준다.
            LikesDto.resLikeDto dto = LikesDto.resLikeDto.builder()
                    .post(post)
                    .member(userDetails.getMember())
                    .like_cnt(likeRepository.countByPostId(likes.get().getPost().getId()))
                    .like_ok(false)
                    .build();
            likeRepository.delete(likes.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        log.info("라이크가 되어 있지 않은 경우 여기서 시작한다.");
        Likes likes1 = likeRepository.save(new Likes(post, userDetails.getMember()));
        LikesDto.resLikeDto dto = LikesDto.resLikeDto.builder()
                .post(likes1.getPost())
                .member(likes1.getMember())
                .like_cnt(likeRepository.countByPostId(likes1.getPost().getId()))
                .like_ok(true)
                .build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
