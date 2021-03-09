package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.application.MemberFindService;
import com.github.suloginscene.authserver.member.application.MemberResponse;
import com.github.suloginscene.authserver.member.application.MemberSignupService;
import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest request) {
        Email email = new Email(request.getEmail());
        Password password = new Password(request.getPassword());
        // TODO confirm password

        Long id = memberSignupService.signup(email, password);

        URI location = linkTo(this.getClass()).slash(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<MemberResponse> getMember(@PathVariable Long id, @AuthenticationPrincipal String username) {
        checkOwner(username, id);

        MemberResponse memberResponse = memberFindService.findMember(id);

        return ResponseEntity.ok().body(memberResponse);
    }

    private void checkOwner(String username, Long memberId) {
        Long audience = Long.parseLong(username);
        if (!audience.equals(memberId)) {
            throw new ResourceAccessDeniedException(username, "member", memberId);
        }
    }

}
