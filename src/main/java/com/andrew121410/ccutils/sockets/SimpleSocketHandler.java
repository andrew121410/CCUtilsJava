package com.andrew121410.ccutils.sockets;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleSocketHandler extends Thread {

    private SimpleSocket simpleSocket;
    @Getter
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
                    this.on = false;
                    out.println("1");
                    break;
                }
                SimpleClientConnection simpleClientConnection = new SimpleClientConnection(this.clientSocket, this.in, this.out, inputLine);
                if (this.simpleSocket.isAsync()) this.simpleSocket.getConsumer().accept(simpleClientConnection);
                else this.simpleSocket.call(simpleClientConnection);
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
