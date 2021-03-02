package com.github.suloginscene.authserver.testing;


public class PhasePrintUtil {

    private static final String FORMAT = "\n======= %s =======\n";


    static void clearStarted() {
        System.out.printf(FORMAT, "clear started");
    }

}
