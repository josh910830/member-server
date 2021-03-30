package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.api.request.MemberSignupRequest;
import com.github.suloginscene.authserver.member.application.MemberFindService;
import com.github.suloginscene.authserver.member.application.MemberSignupService;
import com.github.suloginscene.authserver.member.application.data.MemberData;
import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import com.github.suloginscene.security.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberSignupService memberSignupService;
    private final MemberFindService memberFindService;


    @PostMapping
    ResponseEntity<Void> signup(@RequestBody @Valid MemberSignupRequest request) {
        Email email = new Email(request.getEmail());
        Password password = new Password(request.getPassword());
        // TODO confirm password in fe

        memberSignupService.signup(email, password);

        URI location = linkTo(this.getClass()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    ResponseEntity<MemberData> myInfo(@Authenticated Long memberId) {
        MemberData memberData = memberFindService.findMember(memberId);

        return ResponseEntity.ok().body(memberData);
    }

}
