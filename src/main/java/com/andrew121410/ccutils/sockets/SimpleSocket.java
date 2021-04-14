package com.andrew121410.ccutils.sockets;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class SimpleSocket {

    private Consumer<SimpleClientConnection> consumer;
    private boolean async;

    public void call(SimpleClientConnection simpleClientConnection) {
        this.consumer.accept(simpleClientConnection);
    }

    public void registerHandler(boolean async, Consumer<SimpleClientConnection> consumer) {
        this.async = async;
        this.consumer = consumer;
    }
}
