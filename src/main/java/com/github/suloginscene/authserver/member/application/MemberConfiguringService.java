package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import com.github.suloginscene.authserver.member.domain.Password;
import com.github.suloginscene.mail.MailMessage;
import com.github.suloginscene.mail.Mailer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberConfiguringService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mailer mailer;


    public void changePassword(Long id, Password newPassword) {
        newPassword = newPassword.encoded(passwordEncoder);

        Member member = memberRepository.findById(id);
        member.changePassword(newPassword);
    }


    public void issuePassword(Email email) {
        String randomString = UUID.randomUUID().toString();
        Password newPassword = new Password(randomString);

        newPassword = newPassword.encoded(passwordEncoder);

        Member member = memberRepository.findByEmail(email);
        member.changePassword(newPassword);

        MailMessage mail = randomPasswordMail(member, randomString);
        mailer.send(mail);
    }

    private MailMessage randomPasswordMail(Member member, String randomPasswordValue) {
        String recipient = member.getEmail().get();
        String title = "[Scene] 랜덤 비밀번호 안내";
        String content = "새 비밀번호: " + randomPasswordValue;

        return new MailMessage(recipient, title, content);
    }


    public void withdraw(Long id) {
        memberRepository.deleteById(id);
    }

}
