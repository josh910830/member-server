package com.github.suloginscene.authserver.member.application.data;

import com.github.suloginscene.authserver.member.domain.Member;
import lombok.Data;


@Data
public class MemberData {

    private final String email;


    public MemberData(Member member) {
        email = member.getEmail().get();
    }

}
