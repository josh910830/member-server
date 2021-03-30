package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.application.data.MemberData;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFindingService {

    private final MemberRepository memberRepository;


    public MemberData findMember(Long id) {
        Member member = memberRepository.findById(id);
        return new MemberData(member);
    }

}
