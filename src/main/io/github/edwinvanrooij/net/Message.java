package io.github.edwinvanrooij.net;

import com.google.gson.Gson;

public class Message {
    private static Gson gson = new Gson();

    private int code;
    private String message;

    public Message(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String generateJson(int code, String message) {
        return gson.toJson(new Message(code, message));
    }
}
