package com.github.suloginscene.authserver.jwt.api;

import com.github.suloginscene.authserver.member.application.AuthenticationCommand;
import com.github.suloginscene.authserver.member.application.MemberAuthenticationService;
import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtRestController {

    private final MemberAuthenticationService memberAuthenticationService;


    @PostMapping
    ResponseEntity<Void> issueJwt(@RequestBody JwtRequest request) {
        Email email = new Email(request.getUsername());
        Password password = new Password(request.getPassword());

        AuthenticationCommand command = new AuthenticationCommand(email, password);
        memberAuthenticationService.authenticate(command);

        // TODO implement issue jwt
        return ResponseEntity.ok().build();
    }

}
