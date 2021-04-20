package com.github.suloginscene.memberserver.config;

import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.member.domain.MemberRepository;
import com.github.suloginscene.memberserver.member.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.github.suloginscene.profile.Profiles.LOCAL;


@Component
@Profile(LOCAL)
@RequiredArgsConstructor
public class ManualTestConfigurer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) {
        String email = "scene@email.com";
        String password = passwordEncoder.encode("test_pass");
        Member member = new Member(new Email(email), new Password(password));
        memberRepository.save(member);
    }

}
