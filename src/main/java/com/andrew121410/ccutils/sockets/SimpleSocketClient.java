package com.andrew121410.ccutils.sockets;

import lombok.Getter;

import java.io.IOException;
import java.net.Socket;

public class SimpleSocketClient extends SimpleSocket {

    @Getter
    private Socket socket;
    private SimpleSocketHandler simpleSocketHandler;

    public SimpleSocketClient() {

    }

    public void connect(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.simpleSocketHandler = new SimpleSocketHandler(this, this.socket);
    }

    public void sendMessage(String message) {
        if (simpleSocketHandler.isOn()) {
            this.simpleSocketHandler.getOut().println(message);
        }
    }
}
