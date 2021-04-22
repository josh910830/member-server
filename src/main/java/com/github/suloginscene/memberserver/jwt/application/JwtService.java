package com.github.suloginscene.memberserver.jwt.application;

import com.github.suloginscene.jwt.JwtFactory;
import com.github.suloginscene.memberserver.member.application.MemberIdentifyingService;
import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.memberserver.member.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {

    private final MemberIdentifyingService memberIdentifyingService;


    private final JwtFactory jwtFactory;


    public TokenPair issue(Email email, Password password) {
        Long id = memberIdentifyingService.identify(email, password);

        String jwt = jwtFactory.create(id);
        String refreshToken = UUID.randomUUID().toString();

        return new TokenPair(jwt, refreshToken);
    }

}
