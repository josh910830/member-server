package com.github.suloginscene.authserver.testing.value;

import com.github.suloginscene.authserver.member.domain.Password;


public class Passwords {

    public static Password VALID = new Password("password");
    public static Password ENCODED = new Password(
            "{bcrypt}$2a$10$FmaPpqvyiPMG00XhujnEaOK5MBk46lYgIhgmjQ.zsse7E0osgEs4C");

    public static Password INVALID = new Password("short");

}
