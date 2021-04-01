package com.github.suloginscene.authserver.root.api.representation;

import com.github.suloginscene.authserver.jwt.api.JwtRestController;
import com.github.suloginscene.authserver.member.api.MemberRestController;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class IndexRepresentation extends RepresentationModel<IndexRepresentation> {

    public static IndexRepresentation CONSTANT = new IndexRepresentation();


    private IndexRepresentation() {

        add(linkTo(MemberRestController.class).withRel("signup"));

        add(linkTo(JwtRestController.class).withRel("issueJwt"));

        add(linkTo(MemberRestController.class).slash("/my-info").withRel("myInfo"));

        add(linkTo(MemberRestController.class).slash("/on-forget-password").withRel("onForgetPassword"));

    }

}
