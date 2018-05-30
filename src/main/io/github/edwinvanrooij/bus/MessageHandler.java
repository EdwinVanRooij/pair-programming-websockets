package io.github.edwinvanrooij.bus;

import javax.websocket.Session;

public abstract class MessageHandler {
    public abstract void handleMessage(String text);
}
