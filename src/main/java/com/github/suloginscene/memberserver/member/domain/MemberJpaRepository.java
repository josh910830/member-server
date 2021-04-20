package com.github.suloginscene.memberserver.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


interface MemberJpaRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(Email email);

    Optional<Member> findByEmail(Email email);

}
