package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.Password;
import com.github.suloginscene.authserver.testing.base.IntegrationTest;
import com.github.suloginscene.authserver.testing.data.TestingMembers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@DisplayName("회원 수정 서비스")
class MemberConfiguringServiceTest extends IntegrationTest {

    @Autowired MemberConfiguringService memberConfiguringService;
    @Autowired PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("비밀번호 변경 - 성공")
    void changePassword_onSuccess_updates() {
        Member member = TestingMembers.create();
        given(member);

        Long id = member.getId();
        Password newPassword = new Password("newPassword");
        memberConfiguringService.changePassword(id, newPassword);

        member = sync(member);
        member.checkPassword(newPassword, passwordEncoder);
    }

}
