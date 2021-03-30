package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// TODO changeNames -ing
@Service
@Transactional
@RequiredArgsConstructor
public class MemberConfiguringService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public void changePassword(Long id, Password newPassword) {
        newPassword = newPassword.encoded(passwordEncoder);

        Member member = memberRepository.findById(id);
        member.changePassword(newPassword);
    }

}
