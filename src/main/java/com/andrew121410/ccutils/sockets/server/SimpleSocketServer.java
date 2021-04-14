package com.andrew121410.ccutils.sockets.server;

import lombok.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class SimpleSocketServer {

    @Getter
    private ServerSocket serverSocket;
    private Consumer<SimpleClientConnection> consumer;
    private boolean async;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            theLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerHandler(boolean async, @NonNull Consumer<SimpleClientConnection> consumer) {
        this.async = async;
        this.consumer = consumer;
    }

    private void theLoop() {
        while (true) {
            try {
                new ClientHandler(this, serverSocket.accept());
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

    private void call(SimpleClientConnection simpleClientConnection) {
        this.consumer.accept(simpleClientConnection);
    }

    private class ClientHandler extends Thread {

        private SimpleSocketServer simpleSocketServer;

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(SimpleSocketServer simpleSocketServer, Socket socket) {
            this.simpleSocketServer = simpleSocketServer;
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("1")) {
                        out.println("CLOSED");
                        break;
                    }
                    SimpleClientConnection simpleClientConnection = new SimpleClientConnection(this.clientSocket, this.in, this.out, inputLine);
                    if (this.simpleSocketServer.async) this.simpleSocketServer.consumer.accept(simpleClientConnection);
                    else this.simpleSocketServer.call(simpleClientConnection);
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    public class SimpleClientConnection {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String message;
    }
}