package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;


public class MemberAdapter extends User {

    public MemberAdapter(Member member) {
        super(
                member.getEmail().get(),
                member.getPassword().get(),
                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}
