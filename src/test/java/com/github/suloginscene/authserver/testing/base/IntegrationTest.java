package com.github.suloginscene.authserver.testing.base;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
public abstract class IntegrationTest {

    @Autowired MemberRepository memberRepository;


    protected void given(Member... members) {
        logAround("given", () -> {
            for (Member account : members) {
                memberRepository.save(account);
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
        });
    }


    private void logAround(String info, Runnable runnable) {
        log.debug("{} will be started.", info);
        runnable.run();
        log.debug("{} is finished.", info);
    }

}
