package com.github.suloginscene.authserver.testing;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RepositoryProxy {

    private final MemberRepository memberRepository;


    public void clear() {
        PhasePrintUtil.clearStarted();
        memberRepository.deleteAll();
    }

    public void given(Member member) {
        memberRepository.save(member);
        PhasePrintUtil.givenFinished();
    }

}