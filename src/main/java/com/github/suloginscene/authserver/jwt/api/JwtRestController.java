package com.github.suloginscene.authserver.jwt.api;

import com.github.suloginscene.authserver.member.application.MemberIdentificationService;
import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import com.github.suloginscene.jjwthelper.JwtFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtRestController {

    private final MemberIdentificationService memberIdentificationService;
    private final JwtFactory jwtFactory;


    @PostMapping
    ResponseEntity<String> issueJwt(@RequestBody @Valid JwtRequest request) {
        Email email = new Email(request.getUsername());
        Password password = new Password(request.getPassword());

        Long id = memberIdentificationService.identify(email, password);
        String jwt = jwtFactory.create(id);

        return ResponseEntity.ok().body(jwt);
    }

}
