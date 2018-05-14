package io.github.edwinvanrooij.net;

import io.github.edwinvanrooij.bus.MessageBus;
import io.github.edwinvanrooij.bus.MessageHandler;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

import static io.github.edwinvanrooij.Config.DEFAULT_ENDPOINT;
import static io.github.edwinvanrooij.Config.MAX_IDLE_TIMEOUT;
import static io.github.edwinvanrooij.Util.log;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint(DEFAULT_ENDPOINT)
public class Endpoint {

    private MessageBus bus;
    private Session currentSession;
    private MessageHandler messageHandler;

    @OnOpen
    public void open(Session session) {
        log("Connection '%s' opened.", session.getId());
        session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT * 1000 * 60); // starts at ms --> * 1000 = seconds, * 60 = minutes

        currentSession = session;
        bus = new MessageBus();
        messageHandler = new MessageHandler() {
            @Override
            public void handleMessage(String text) {
                log("From 'handleMessage' -> queue: '%s'", text);
            }
        };
        bus.consumeMessage(messageHandler);
    }

    @OnClose
    public void close(Session session) {
        log("Connection '%s' closed.", session.getId());
    }

    @OnError
    public void onError(Throwable error) {
        log("Error in session: %s", error.getMessage());
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        log("From 'handleMessage' -> websocket '%s': %s", session.getId(), message);
        bus.produceMessage(message);
    }
}
