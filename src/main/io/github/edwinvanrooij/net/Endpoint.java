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
        session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT * 1000 * 60); // starts at ms --> * 1000 = seconds, * 60 = minutes
        System.out.println(String.format("A new client has connected: %s", session.getId()));
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
    public void handleMessage(String text, Session session) {
        Event e = Event.fromText(text);

        switch (e.getVersion()) {
            case Event.VERSION_1:
                System.out.println("Handling event for version 1.");
                handleEventVersion1(e, session);
                break;

            case Event.VERSION_2:
                System.out.println("Handling event for version 2.");
                handleEventVersion2(e, session);
                break;

            default:
                System.out.println(String.format("Could not figure out event version for '%s'.", e.getVersion()));

        }
    }

    private void handleEventVersion1(Event e, Session s) {
        switch (e.getType()) {
            case Event.EVENT_JOIN_EXCHANGE:
                // Initialize MessageBus and handle new messages from queue
                bus = new MessageBus(e.getMessage());
                bus.consumeMessage(new MessageHandler() {
                    @Override
                    public void handleMessage(String text) {
                        try {
                            if (s.isOpen()) {
                                System.out.println(String.format("S is open, sending '%s' back", text));
                                s.getBasicRemote().sendText(Event.generateNewMessage(text));
                            } else {
                                System.out.println("S is NOT open.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, s.getId());
                System.out.println(String.format("'%s' joined queue '%s'", s.getId(), e.getMessage()));
                break;

            case Event.EVENT_NEW_MESSAGE:
                // New message received
                System.out.println(String.format("Received new event - new message: %s", e.getMessage()));
                bus.produceMessage(e.getMessage());
                break;

            default:
                System.out.println("Could not figure out what kind of event this was.");
        }
    }

    private void handleEventVersion2(Event e, Session s) {
        System.out.println(String.format("Event version 2 is not yet implemented. Stay tuned.\nEvent was '%s'", e.toString()));
    }
}
