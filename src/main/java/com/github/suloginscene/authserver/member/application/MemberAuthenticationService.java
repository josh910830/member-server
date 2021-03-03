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
public class MemberAuthenticationService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public void authenticate(AuthenticationCommand command) {
        Email email = command.getEmail();
        Member member = findMember(email);

        Password password = command.getPassword();
        member.checkPassword(password, passwordEncoder);
    }

    private Member findMember(Email email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email.get()));
    }

}
