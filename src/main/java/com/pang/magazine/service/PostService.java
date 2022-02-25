package com.pang.magazine.service;

import com.pang.magazine.domain.Likes;
import com.pang.magazine.domain.Member;
import com.pang.magazine.domain.Post;
import com.pang.magazine.dto.PostDto;
import com.pang.magazine.exception.exceptionclass.NotAuthenticationException;
import com.pang.magazine.exception.exceptionclass.NotFoundException;
import com.pang.magazine.exception.exceptionclass.NotUpdateWriterException;
import com.pang.magazine.repository.LikeRepository;
import com.pang.magazine.repository.MemberRepository;
import com.pang.magazine.repository.PostRepository;
import com.pang.magazine.security.UserDetailsImpl;
import com.pang.magazine.util.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 조회
    @Transactional(readOnly = true)
    public ResponseEntity getAll(UserDetailsImpl userDetails){
        CustomResponseEntity response;
        List<PostDto.resAllPostsDto> resDto = new ArrayList<>();
        List<Post> postList = postRepository.findAll();

            // 로그인 하지 않은 사용자
            for (Post post : postList){
                PostDto.resAllPostsDto dto = PostDto.resAllPostsDto.builder()
                        .post_id(post.getId())
                        .name(post.getMember().getName())
                        .contents(post.getContents())
                        .img_url(post.getImg_url())
                        .like_cnt(likeRepository.countByPostId(post.getId()))
                        .create_date(String.valueOf(post.getCreateAt()))
                        .modified_date(String.valueOf(post.getModifiedAt()))
                        .like_ok(false)
                        .build();

                if (userDetails != null){
                    // 유저id, 게시글 번호 ==> 두개로 유게 게시글을 체크했는지 확인한다.
                    Optional<Likes> byId = likeRepository.findByUserIdAndPostId
                            (userDetails.getMember().getId(), post.getId());
                    if (byId.isPresent()) {
                        dto.setLike_ok(true);
                    }
                }
                //list에 dto들을 계속해서 쌓는다.
                resDto.add(dto);
            }

        response = CustomResponseEntity.builder()
                .message("게시글 조회 성공")
                .code(HttpStatus.OK)
                .data(resDto)
                .build();

        return response.responseAll();
    }

    // 생성
    @Transactional
    public ResponseEntity createPost(PostDto.reqCreateDto dto, Principal principal) {

        CustomResponseEntity response;

        Member member = memberRepository.findByUsername(principal.getName());
        Post post = Post.builder()
                .dto(dto)
                .member(member)
                .build();

        Post saved = postRepository.save(post);

        response = CustomResponseEntity.builder()
                .code(HttpStatus.OK)
                .data(saved)
                .message("게시글 작성완료")
                .build();

        return response.responseAll();
//        return  new ResponseEntity<>("게시글 작성완료", HttpStatus.OK);
    }

    // 수정
    @Transactional
    public ResponseEntity updatePost(Long postId, PostDto.reqUpdateDto dto, Principal principal) throws AuthenticationException {
        CustomResponseEntity response;

        Optional<Post> byId = postRepository.findById(postId);
        if(byId.isEmpty()){
            throw new NotFoundException("없는 게시글.");
        }
        // 갖고온 post의 작성자가 맞는지 확인하기
        Post post = byId.get();
        if (!post.getMember().getUsername().equals(principal.getName())){
            throw new NotAuthenticationException("작성자가 아니라 수정 불가합니다.");
        }
        PostDto updated = post.update(dto);
        response = CustomResponseEntity.builder()
                .message("수정완료")
                .data(updated)
                .code(HttpStatus.OK)
                .build();
        return response.responseAll();
    }

    // 삭제
    @Transactional
    public ResponseEntity deletePost(Long postId, Principal principal) throws AuthenticationException {
        CustomResponseEntity response;
        Optional<Post> byId = postRepository.findById(postId);
        if(byId.isEmpty()){
            throw new NotFoundException("없는 게시글.");
        }
        // 갖고온 post의 작성자가 맞는지 확인하기
        Post post = byId.get();
        if (!post.getMember().getUsername().equals(principal.getName())){
            throw new NotAuthenticationException("작성자가 아니라 삭제 불가합니다.");
        }
        // 삭제해놓기
        postRepository.deleteById(postId);
        response = CustomResponseEntity.builder()
                .code(HttpStatus.OK)
                .message("삭제완료")
                .build();
        return response.responseNotData();
    }

}
