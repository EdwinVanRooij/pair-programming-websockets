package io.github.edwinvanrooij.net;

import com.google.gson.Gson;

public class Event {
    private static Gson gson = new Gson();

    public static final String VERSION_1 = "1.0.0";
    public static final String VERSION_2 = "2.0.0";

    public static final String EVENT_JOIN_EXCHANGE = "JOIN_EXCHANGE";
    public static final String EVENT_NEW_MESSAGE = "NEW_MESSAGE";

    private String version;
    private String type;
    private String message;

    public Event(String version, String type, String message) {
        this.version = version;
        this.type = type;
        this.message = message;
    }

    public static Gson getGson() {
        return gson;
    }

    public static void setGson(Gson gson) {
        Event.gson = gson;
    }

    public static String getEventJoinExchange() {
        return EVENT_JOIN_EXCHANGE;
    }

    public static String getEventNewMessage() {
        return EVENT_NEW_MESSAGE;
    }

    public String getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Event fromText(String text) {
        return gson.fromJson(text, Event.class);
    }

    public static String generateNewMessage(String message) {
        return gson.toJson(new Event(VERSION_1, EVENT_NEW_MESSAGE, message));
    }

    @Override
    public String toString() {
        return "Event{" +
                "version='" + version + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

