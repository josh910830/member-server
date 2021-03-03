package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.db.RepositoryProxy;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@DisplayName("회원찾기 서비스 (UserDetailsService)")
class MemberDetailsServiceTest {

    @Autowired MemberDetailsService memberDetailsService;

    @Autowired RepositoryProxy repositoryProxy;

    Member member;
    String email;


    @BeforeEach
    void setup() {
        member = DefaultMembers.create();
        email = DefaultMembers.EMAIL;
    }

    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    @DisplayName("정상 - 멤버어댑터 반환")
    void loadByUsername_onExistent_returnsUser() {
        repositoryProxy.given(member);

        UserDetails userDetails = memberDetailsService.loadUserByUsername(email);

        assertThat(userDetails).isInstanceOf(MemberAdapter.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 - 오류 발생")
    void loadByUsername_onNonExistent_throwsException() {
        Executable action = () -> memberDetailsService.loadUserByUsername(email);

        assertThrows(UsernameNotFoundException.class, action);
    }

}
