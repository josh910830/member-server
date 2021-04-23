package com.github.suloginscene.memberserver.root.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RootForwardController {

    @GetMapping("/")
    String root() {
        return "forward:/docs/index.html";
    }

}
