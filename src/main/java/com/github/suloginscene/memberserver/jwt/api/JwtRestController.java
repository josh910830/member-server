package com.github.suloginscene.memberserver.jwt.api;

import com.github.suloginscene.memberserver.jwt.application.JwtService;
import com.github.suloginscene.memberserver.jwt.application.TokenPair;
import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.memberserver.member.domain.Password;
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

    public static final String PATH = "/jwt";

    private final JwtService jwtService;


    @PostMapping
    ResponseEntity<JwtResponse> issueJwt(@RequestBody @Valid JwtRequest request) {
        Email email = new Email(request.getUsername());
        Password password = new Password(request.getPassword());

        TokenPair tokenPair = jwtService.issue(email, password);

        return ResponseEntity.ok().body(new JwtResponse(tokenPair));
    }

    @PostMapping("/renew")
    ResponseEntity<JwtResponse> renewJwt(@RequestBody String refreshToken) {
        TokenPair tokenPair = jwtService.renew(refreshToken);
        return ResponseEntity.ok().body(new JwtResponse(tokenPair));
    }

}
