package com.andrew121410.ccutils.sockets;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleSocketHandler extends Thread {

    private SimpleSocket simpleSocket;
    @Getter
    @Setter
    private boolean on = true;

    private Socket clientSocket;
    @Getter
    private PrintWriter out;
    @Getter
    private BufferedReader in;

    public SimpleSocketHandler(SimpleSocket simpleSocket, Socket socket) {
        this.simpleSocket = simpleSocket;
        this.clientSocket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("1")) {
                    out.println("1");
                    close();
                } else if (inputLine.equals("0")) {
                    simpleSocket.receivedHeartbeat(this);
                    out.println("0");
                    return;
                }
                SimpleClientConnection simpleClientConnection = new SimpleClientConnection(this.clientSocket, this.in, this.out, inputLine);
                if (this.simpleSocket.isAsync()) this.simpleSocket.getConsumer().accept(simpleClientConnection);
                else this.simpleSocket.call(simpleClientConnection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        on = false;
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception ignored) {
        }
    }

    public synchronized void sendMessage(String message) {
        if (this.isOn())
            this.out.println(message);
    }
}
