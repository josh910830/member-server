package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.api.request.MemberPasswordChangeRequest;
import com.github.suloginscene.authserver.member.api.request.MemberSignupRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    ResponseEntity<Void> signup(@RequestBody @Valid MemberSignupRequest request) {
        Email email = new Email(request.getEmail());
        Password password = new Password(request.getPassword());

        memberSignupService.signup(email, password);

        URI location = linkTo(this.getClass()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/verify")
    ResponseEntity<Void> verify(@RequestParam Long id, @RequestParam String token) {
        memberSignupService.verify(id, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-info")
    ResponseEntity<MemberData> myInfo(@Authenticated Long memberId) {
        MemberData memberData = memberFindingService.findMember(memberId);

        return ResponseEntity.ok().body(memberData);
    }

    @GetMapping("/on-forget-password")
    ResponseEntity<Void> onForgetPassword(@RequestParam String email) {
        Email target = new Email(email);

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
