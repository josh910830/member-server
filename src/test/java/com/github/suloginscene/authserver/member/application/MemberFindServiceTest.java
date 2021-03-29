package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.application.data.MemberData;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.base.IntegrationTest;
import com.github.suloginscene.authserver.testing.data.TestingMembers;
import com.github.suloginscene.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("회원 찾기 서비스")
class MemberFindServiceTest extends IntegrationTest {

    @Autowired MemberFindService memberFindService;


    @Test
    @DisplayName("성공 - DTO 반환")
    void find_onSuccess_returnsDto() {
        Member member = TestingMembers.create();
        given(member);

        Long id = member.getId();
        MemberData memberData = memberFindService.findMember(id);

        assertThat(memberData).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("리소스 없음 - 예외 발생")
    void find_onNonExistent_throwsException() {
        Long nonExistentId = Long.MAX_VALUE;
        Executable action = () -> memberFindService.findMember(nonExistentId);

        assertThrows(NotFoundException.class, action);
    }

}
