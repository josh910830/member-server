package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.Data;


@Data
public class AuthenticationCommand {

    private final Email email;
    private final Password password;

}
