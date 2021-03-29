package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import com.github.suloginscene.authserver.member.domain.Password;
import com.github.suloginscene.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberSignupService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Long signup(Email email, Password password) {
        checkDuplicated(email);
        password = password.encoded(passwordEncoder);

        Member created = new Member(email, password);
        return memberRepository.save(created);
    }

    private void checkDuplicated(Email email) {
        if (memberRepository.existsByEmail(email)) {
            throw new RequestException(email + " is already exists");
        }
    }

}
