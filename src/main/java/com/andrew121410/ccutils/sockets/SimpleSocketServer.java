package com.andrew121410.ccutils.sockets;

import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;

public class SimpleSocketServer extends SimpleSocket {

    @Getter
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        theLoop();
    }

    private void theLoop() {
        while (true) {
            try {
                new SimpleSocketHandler(this, serverSocket.accept());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}