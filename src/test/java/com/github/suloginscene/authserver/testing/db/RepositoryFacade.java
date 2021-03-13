package com.github.suloginscene.authserver.testing.db;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RepositoryFacade {

    private static final String FORMAT = "======= %s =======\n";

    private final MemberRepository memberRepository;


    public void given(Member member) {
        memberRepository.save(member);
        printGivenFinished();
    }

    public void clear() {
        printClearStarted();
        memberRepository.deleteAll();
    }


    private void printGivenFinished() {
        System.out.printf(FORMAT, "given finished");
        System.out.println();
    }

    private void printClearStarted() {
        System.out.println();
        System.out.printf(FORMAT, "clear started");
    }

}
