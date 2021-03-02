package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.application.MemberSignupService;
import com.github.suloginscene.authserver.member.application.SignupCommand;
import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberSignupService memberSignupService;


    @PostMapping
    ResponseEntity<Void> signup(SignupRequest request) {
        Email email = new Email(request.getEmail());
        Password password = new Password(request.getPassword());

        SignupCommand command = new SignupCommand(email, password);
        Long id = memberSignupService.signup(command);

        URI location = linkTo(this.getClass()).slash(id).toUri();
        return ResponseEntity.created(location).build();
    }

}
