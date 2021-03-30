package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.config.AppProperties;
import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import com.github.suloginscene.authserver.member.domain.Password;
import com.github.suloginscene.authserver.member.domain.temp.TempMember;
import com.github.suloginscene.authserver.member.domain.temp.TempMemberRepository;
import com.github.suloginscene.exception.RequestException;
import com.github.suloginscene.mail.MailMessage;
import com.github.suloginscene.mail.Mailer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberSignupService {

    private final TempMemberRepository tempMemberRepository;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final Mailer mailer;

    private final AppProperties appProperties;


    public void signup(Email email, Password password) {
        checkDuplicated(email);
        password = password.encoded(passwordEncoder);

        TempMember created = new TempMember(email, password);
        TempMember saved = tempMemberRepository.save(created);

        MailMessage mail = verificationMail(saved);
        mailer.send(mail);
    }

    private void checkDuplicated(Email email) {
        if (memberRepository.existsByEmail(email)) {
            throw new RequestException(email + " is already exists");
        }
        if (tempMemberRepository.existsByEmail(email)) {
            throw new RequestException(email + " is already exists");
        }
    }

    private MailMessage verificationMail(TempMember tempMember) {
        String recipient = tempMember.getEmail().get();
        String title = "[Scene] 회원가입 인증 메일";

        String id = tempMember.getId().toString();
        String token = tempMember.getVerificationToken();
        String queryString = "id=" + id + "&token=" + token;

        String address = appProperties.getAddress();
        String link = address + "/verify?" + queryString;
        String content = "회원가입 인증 링크: " + link;

        return new MailMessage(recipient, title, content);
    }

}
