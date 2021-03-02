package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberSignupService {

    private final MemberRepository memberRepository;


    public Long signup(SignupCommand command) {
        Email email = command.getEmail();
        Password password = command.getPassword();

        Member created = new Member(email, password);
        Member saved = memberRepository.save(created);

        return saved.getId();
    }

}
