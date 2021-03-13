package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.db.RepositoryFacade;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@DisplayName("회원 찾기 서비스")
class MemberFindServiceTest {

    @Autowired MemberFindService memberFindService;

    @Autowired RepositoryFacade repositoryFacade;

    Member member;


    @BeforeEach
    void setup() {
        member = DefaultMembers.create();
    }

    @AfterEach
    void clear() {
        repositoryFacade.clear();
    }


    @Test
    @DisplayName("성공 - DTO 반환")
    void find_onSuccess_returnsDto() {
        repositoryFacade.given(member);

        MemberResponse memberResponse = memberFindService.findMember(member.getId());

        assertThat(memberResponse).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("리소스 없음 - 예외 발생")
    void find_onNonExistent_throwsException() {
        Long nonExistentId = 1L;
        Executable action = () -> memberFindService.findMember(nonExistentId);

        assertThrows(NoSuchElementException.class, action);
    }

}
