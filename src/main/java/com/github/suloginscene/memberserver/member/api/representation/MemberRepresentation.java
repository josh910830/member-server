package com.github.suloginscene.memberserver.member.api.representation;

import com.github.suloginscene.memberserver.member.api.MemberRestController;
import com.github.suloginscene.memberserver.member.application.data.MemberData;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static com.github.suloginscene.string.HrefAssembleUtil.href;


public class MemberRepresentation extends RepresentationModel<MemberRepresentation> {

    @Getter
    private final String email;


    public MemberRepresentation(MemberData member) {
        email = member.getEmail();

        add(Link.of(href(MemberRestController.PATH)).withRel("changePassword"));
        add(Link.of(href(MemberRestController.PATH)).withRel("withdraw"));
    }

}
