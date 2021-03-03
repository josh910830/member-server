package com.github.suloginscene.authserver.jwt.api;

import com.github.suloginscene.authserver.jwt.application.JwtFactory;
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
    private final JwtFactory jwtFactory;


    @PostMapping
    ResponseEntity<String> issueJwt(@RequestBody JwtRequest request) {
        Email email = new Email(request.getUsername());
        Password password = new Password(request.getPassword());

        AuthenticationCommand command = new AuthenticationCommand(email, password);
        Long id = memberAuthenticationService.authenticate(command);

        String jwt = jwtFactory.create(id);
        return ResponseEntity.ok().body(jwt);
    }

}
