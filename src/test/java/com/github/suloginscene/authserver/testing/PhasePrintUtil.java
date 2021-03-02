package com.github.suloginscene.authserver.testing;


class PhasePrintUtil {

    private static final String FORMAT = "======= %s =======\n";


    static void givenFinished() {
        System.out.printf(FORMAT, "given finished");
        System.out.println();
    }

    static void clearStarted() {
        System.out.println();
        System.out.printf(FORMAT, "clear started");
    }

}
