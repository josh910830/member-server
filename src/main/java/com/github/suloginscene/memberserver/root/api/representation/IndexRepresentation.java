package com.github.suloginscene.memberserver.root.api.representation;

import com.github.suloginscene.memberserver.jwt.api.JwtRestController;
import com.github.suloginscene.memberserver.member.api.MemberRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static com.github.suloginscene.string.HrefAssembleUtil.href;


public class IndexRepresentation extends RepresentationModel<IndexRepresentation> {

    public static IndexRepresentation CONSTANT = new IndexRepresentation();


    private IndexRepresentation() {
        add(Link.of(href(MemberRestController.PATH)).withRel("signup"));
        add(Link.of(href(JwtRestController.PATH)).withRel("issueJwt"));
        add(Link.of(href(MemberRestController.PATH + "/my-info")).withRel("myInfo"));
        add(Link.of(href(MemberRestController.PATH + "/on-forget-password")).withRel("onForgetPassword"));
    }

}
