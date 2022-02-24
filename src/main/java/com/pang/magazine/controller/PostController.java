package com.pang.magazine.controller;

import com.pang.magazine.dto.PostDto;
import com.pang.magazine.repository.PostRepository;
import com.pang.magazine.security.UserDetailsImpl;
import com.pang.magazine.service.PostService;
import com.pang.magazine.util.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;
    // 생성
    @PostMapping("/api/posts")
    public ResponseEntity createPost(@Valid @RequestBody PostDto.reqCreateDto dto,
                                     Principal principal){
        CustomResponseEntity response;
        // 비로그인 회원은 작성 할 수 없음.
        if(principal == null){
            response = CustomResponseEntity.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message("로그인 사용자만 가능합니다.")
                    .build();
            return response.responseNotData();
        } else{
            // 로그인 되어 있는 경우 그대로 게시글 생성하기
            return postService.createPost(dto, principal);
        }
    }
    // 삭제
    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable("postId") Long postId,
                                     Principal principal) throws AuthenticationException {
        ResponseEntity response;
        if(principal == null){
            throw new AuthenticationException("수정 권한이 없습니다.");
        }
        return postService.deletePost(postId, principal);
    }


    // 수정
    @PatchMapping("/api/posts/{postId}")
    public ResponseEntity updatePost(@PathVariable("postId") Long postId,
                                     @Valid @RequestBody PostDto.reqUpdateDto dto,
                                     Principal principal) throws AuthenticationException {

        ResponseEntity response;

        if(principal == null){
            throw new AuthenticationException("수정 권한이 없습니다.");
        }
        return postService.updatePost(postId, dto, principal);

    }
    // 조회
    @GetMapping("/api/posts")
    public ResponseEntity getAllPost(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.getAll(userDetails);
    }
}
