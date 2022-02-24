package com.pang.magazine.dto;

import com.pang.magazine.domain.Member;
import com.pang.magazine.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class LikesDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @Setter
    public static class resLikeDto{
        // 줘야 할게 무엇인가?
        private Post post;
        private Member member;
        private Boolean like_ok;
        private int like_cnt;
    }
}
