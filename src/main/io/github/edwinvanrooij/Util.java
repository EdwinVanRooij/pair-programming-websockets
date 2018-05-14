package io.github.edwinvanrooij;

/**
 * Created by eddy
 * on 7/18/17.
 */
public class Util {

    public static void log(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    public static void logError(Throwable e) {
        System.out.println(String.format("Error: %s", e));
    }

}
