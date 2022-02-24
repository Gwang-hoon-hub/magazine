package com.pang.magazine.repository;

import com.pang.magazine.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username = :username")
    Member findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}
