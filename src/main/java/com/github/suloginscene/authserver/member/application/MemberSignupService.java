package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import com.github.suloginscene.authserver.member.domain.Password;
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


    public Long signup(SignupCommand command) {
        Email email = command.getEmail();
        checkDuplicated(email);

        Password password = command.getPassword();
        password = password.encoded(passwordEncoder);

        Member created = new Member(email, password);
        Member saved = memberRepository.save(created);

        return saved.getId();
    }

    private void checkDuplicated(Email email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
    }

}
