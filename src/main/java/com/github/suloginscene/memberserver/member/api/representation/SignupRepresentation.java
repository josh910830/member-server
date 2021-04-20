package com.github.suloginscene.memberserver.member.api.representation;

import com.github.suloginscene.memberserver.member.api.MemberRestController;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static com.github.suloginscene.string.HrefAssembleUtil.href;


@Data @EqualsAndHashCode(callSuper = false)
public class SignupRepresentation extends RepresentationModel<SignupRepresentation> {

    public SignupRepresentation(Long id) {
        add(Link.of(href(MemberRestController.PATH + "/verify/" + id)).withRel("verify"));
    }

}
