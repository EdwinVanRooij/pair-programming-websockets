package io.github.edwinvanrooij.net;

public class Message {
    private String message;
    private String exchangeName;

    public Message(String message, String exchangeName) {
        this.message = message;
        this.exchangeName = exchangeName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
