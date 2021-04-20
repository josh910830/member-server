package com.github.suloginscene.memberserver.root.api;

import com.github.suloginscene.memberserver.root.api.representation.IndexRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class RootRestController {

    @GetMapping
    ResponseEntity<IndexRepresentation> getIndex() {
        return ResponseEntity.ok(IndexRepresentation.CONSTANT);
    }

}
