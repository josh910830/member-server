package com.github.suloginscene.authserver.jwt.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtRestController {

    @PostMapping
    ResponseEntity<Void> issueJwt(@RequestBody JwtRequest request) {
        // TODO implement issue jwt
        return ResponseEntity.ok().build();
    }

}
