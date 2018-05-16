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

    @OnOpen
    public void open(Session session) throws IOException {
        // WS connection opened
        log("Connection '%s' opened.", session.getId());
        session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT * 1000 * 60); // starts at ms --> * 1000 = seconds, * 60 = minutes

        // Send a message back; success!
        session.getBasicRemote().sendText(Message.generateJson("Successfully opened connection!"));

        // Initialize MessageBus and handle new messages from queue
        bus = new MessageBus(session);
        bus.consumeMessage(new MessageHandler() {
            @Override
            public void handleMessage(Session session, String text) {
                log("From 'handleMessage' -> queue: '%s'", text);
                try {
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(Message.generateJson(text));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
