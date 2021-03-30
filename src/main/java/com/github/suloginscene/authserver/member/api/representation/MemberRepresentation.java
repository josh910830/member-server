package com.github.suloginscene.authserver.member.api.representation;

import com.github.suloginscene.authserver.member.api.MemberRestController;
import com.github.suloginscene.authserver.member.application.data.MemberData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Data @EqualsAndHashCode(callSuper = false)
public class MemberRepresentation extends RepresentationModel<MemberRepresentation> {

    private final String email;


    public MemberRepresentation(MemberData member) {
        email = member.getEmail();

        add(linkTo(MemberRestController.class).withRel("change-password"));
        add(linkTo(MemberRestController.class).withRel("withdraw"));
    }

}
