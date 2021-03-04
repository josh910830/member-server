package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Member;
import lombok.Data;


@Data
public class MemberResponse {

    private final String email;


    public MemberResponse(Member member) {
        email = member.getEmail().get();
    }

}
