package com.github.suloginscene.authserver.member.api.representation;

import com.github.suloginscene.authserver.member.api.MemberRestController;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Data @EqualsAndHashCode(callSuper = false)
public class SignupRepresentation extends RepresentationModel<SignupRepresentation> {

    private final Long id;


    public SignupRepresentation(Long id) {
        this.id = id;

        add(linkTo(MemberRestController.class).slash("verify").withRel("verify"));
    }

}
