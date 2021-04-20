package com.github.suloginscene.memberserver.member.api.representation;

import com.github.suloginscene.memberserver.member.api.MemberRestController;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Data @EqualsAndHashCode(callSuper = false)
public class SignupRepresentation extends RepresentationModel<SignupRepresentation> {

    public SignupRepresentation(Long id) {
        add(linkTo(MemberRestController.class).slash("verify").slash(id).withRel("verify"));
    }

}
