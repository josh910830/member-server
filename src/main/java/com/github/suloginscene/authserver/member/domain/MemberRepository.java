package com.github.suloginscene.authserver.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(Email email);

}
