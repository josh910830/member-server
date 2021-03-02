package com.github.suloginscene.authserver.testing.value;

import com.github.suloginscene.authserver.member.domain.Password;


public class Passwords {

    public static Password VALID = new Password("password");
    public static Password INVALID = new Password("short");

}
