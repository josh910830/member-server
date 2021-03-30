package com.github.suloginscene.authserver.testing.base;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import com.github.suloginscene.authserver.member.domain.temp.TempMember;
import com.github.suloginscene.authserver.member.domain.temp.TempMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
public abstract class IntegrationTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TempMemberRepository tempMemberRepository;


    protected void given(Member... members) {
        logAround("given", () -> {
            for (Member member : members) {
                memberRepository.save(member);
            }
        });
    }

    protected void given(TempMember... tempMembers) {
        logAround("given", () -> {
            for (TempMember tempMember : tempMembers) {
                tempMemberRepository.save(tempMember);
            }
        });
    }


    protected Member sync(Member member) {
        ThreadLocal<Member> temp = new ThreadLocal<>();
        logAround("sync", () -> {
            Member found = memberRepository.findById(member.getId());
            temp.set(found);
        });
        return temp.get();
    }


    @AfterEach
    final void clearAllRepositories() {
        logAround("clear", () -> {
            memberRepository.deleteAll();
            tempMemberRepository.deleteAll();
        });
    }


    private void logAround(String info, Runnable runnable) {
        log.debug("{} will be started.", info);
        runnable.run();
        log.debug("{} is finished.", info);
    }

}