package com.pang.magazine.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
@Entity
@Table(name = "Member")
public class Member extends Timestamped implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Builder
    public Member(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

}
