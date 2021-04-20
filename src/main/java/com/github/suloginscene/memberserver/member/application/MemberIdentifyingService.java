package com.github.suloginscene.memberserver.member.application;

import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.member.domain.MemberRepository;
import com.github.suloginscene.memberserver.member.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberIdentifyingService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Long identify(Email email, Password password) {
        Member member = memberRepository.findByEmail(email);
        member.checkPassword(password, passwordEncoder);

        return member.getId();
    }

}
