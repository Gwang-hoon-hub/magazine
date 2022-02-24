package com.pang.magazine.controller;

import com.pang.magazine.security.UserDetailsImpl;
import com.pang.magazine.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/api/favorite/{postId}")
    public ResponseEntity createLikes(@PathVariable("postId") Long postId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) throws AuthenticationException {
        return likesService.createLikes(postId, userDetails);
      }
}
