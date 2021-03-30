package com.github.suloginscene.authserver.member.domain.temp;

import com.github.suloginscene.authserver.member.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;


interface TempMemberJpaRepository extends JpaRepository<TempMember, Long> {

    boolean existsByEmail(Email email);

}
