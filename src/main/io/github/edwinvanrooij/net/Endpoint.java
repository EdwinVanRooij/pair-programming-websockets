package io.github.edwinvanrooij.net;

import io.github.edwinvanrooij.Const;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static io.github.edwinvanrooij.Config.MAX_IDLE_TIMEOUT_HOST;
import static io.github.edwinvanrooij.Util.log;

/**
 * Created by eddy
 * on 6/5/17.
 */

@ServerEndpoint(Const.DEFAULT_ENDPOINT)
public class Endpoint {

    @OnOpen
    public void open(Session session) {
        log("%s host connection opened.");
        session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT_HOST * 1000 * 60); // starts at ms --> * 1000 = seconds, * 60 = minutes
    }

    @OnClose
    public void close(Session session) {
        log("Host connection closed.");
    }

    @OnError
    public void onError(Throwable error) {
        log(String.format("Error: %s", error.getMessage()));
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        log(String.format("Received from: %s", message));
    }
}
