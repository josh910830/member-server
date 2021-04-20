package com.github.suloginscene.memberserver.jwt.api;

import com.github.suloginscene.jwt.JwtFactory;
import com.github.suloginscene.memberserver.jwt.api.request.JwtRequest;
import com.github.suloginscene.memberserver.member.application.MemberIdentifyingService;
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

    private final MemberIdentifyingService memberIdentifyingService;
    private final JwtFactory jwtFactory;


    @PostMapping
    ResponseEntity<String> issueJwt(@RequestBody @Valid JwtRequest request) {
        Email email = new Email(request.getUsername());
        Password password = new Password(request.getPassword());

        Long id = memberIdentifyingService.identify(email, password);
        String jwt = jwtFactory.create(id);

        return ResponseEntity.ok().body(jwt);
    }

}
