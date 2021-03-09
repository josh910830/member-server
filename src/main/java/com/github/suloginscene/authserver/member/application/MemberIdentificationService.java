package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberIdentificationService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Long identify(Email email, Password password) {
        Member member = findMember(email);
        member.checkPassword(password, passwordEncoder);

        return member.getId();
    }

    private Member findMember(Email email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email.get()));
    }

}
