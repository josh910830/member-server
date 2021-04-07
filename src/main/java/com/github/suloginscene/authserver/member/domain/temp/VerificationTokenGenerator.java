package com.github.suloginscene.authserver.member.domain.temp;


class VerificationTokenGenerator {

    private static final int TOKEN_LENGTH = 6;

    private static final int ALPHABET_COUNT = 26;
    private static final int UNICODE_A = 65;


    static String generate() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            double randomNumber = Math.random() * ALPHABET_COUNT;
            char randomLetter = (char) (UNICODE_A + randomNumber);
            sb.append(randomLetter);
        }
        return sb.toString();
    }

}
