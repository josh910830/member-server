package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.api.representation.MemberRepresentation;
import com.github.suloginscene.authserver.member.api.representation.SignupRepresentation;
import com.github.suloginscene.authserver.member.api.request.MemberOnForgetPasswordRequest;
import com.github.suloginscene.authserver.member.api.request.MemberPasswordChangeRequest;
import com.github.suloginscene.authserver.member.api.request.MemberSignupRequest;
import com.github.suloginscene.authserver.member.api.request.MemberVerificationRequest;
import com.github.suloginscene.authserver.member.application.MemberConfiguringService;
import com.github.suloginscene.authserver.member.application.MemberFindingService;
import com.github.suloginscene.authserver.member.application.MemberSignupService;
import com.github.suloginscene.authserver.member.application.data.MemberData;
import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import com.github.suloginscene.security.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final MemberFindingService memberFindingService;
    private final MemberConfiguringService memberConfiguringService;


    @PostMapping
    ResponseEntity<SignupRepresentation> signup(@RequestBody @Valid MemberSignupRequest request) {
        Email email = new Email(request.getUsername());
        Password password = new Password(request.getPassword());

        Long id = memberSignupService.signup(email, password);

        SignupRepresentation representation = new SignupRepresentation(id);
        return ResponseEntity.ok().body(representation);
    }

    @PostMapping("/verify/{id}")
    ResponseEntity<Void> verify(@PathVariable Long id,
                                @RequestBody @Valid MemberVerificationRequest request) {
        String token = request.getToken();

        memberSignupService.verify(id, token);

        URI location = linkTo(this.getClass()).slash("my-info").toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/my-info")
    ResponseEntity<MemberRepresentation> myInfo(@Authenticated Long memberId) {
        MemberData member = memberFindingService.findMember(memberId);

        MemberRepresentation memberRepresentation = new MemberRepresentation(member);
        return ResponseEntity.ok().body(memberRepresentation);
    }

    @PostMapping("/on-forget-password")
    ResponseEntity<Void> onForgetPassword(@RequestBody @Valid MemberOnForgetPasswordRequest request) {
        Email target = new Email(request.getUsername());

        memberConfiguringService.onForgetPassword(target);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    ResponseEntity<Void> changePassword(@RequestBody @Valid MemberPasswordChangeRequest request,
                                        @Authenticated Long memberId) {
        Password newPassword = new Password(request.getNewPassword());

        memberConfiguringService.changePassword(memberId, newPassword);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    ResponseEntity<Void> withdraw(@Authenticated Long memberId) {
        memberConfiguringService.withdraw(memberId);

        return ResponseEntity.noContent().build();
    }

}
