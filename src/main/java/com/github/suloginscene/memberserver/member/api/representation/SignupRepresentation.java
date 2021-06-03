package com.github.suloginscene.memberserver.member.api.representation;

import com.github.suloginscene.memberserver.member.api.MemberRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static com.github.suloginscene.string.HrefAssembleUtil.href;


public class SignupRepresentation extends RepresentationModel<SignupRepresentation> {

    public SignupRepresentation(Long id) {
        add(Link.of(href(MemberRestController.PATH + "/verify/" + id)).withRel("verify"));
    }

}
