package io.github.edwinvanrooij.net;

import com.google.gson.Gson;

public class Message {
    private static Gson gson = new Gson();

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public static String generateJson(String message) {
        return gson.toJson(new Message(message));
    }
}
