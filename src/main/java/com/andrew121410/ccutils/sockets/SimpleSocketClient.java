package com.andrew121410.ccutils.sockets;

import lombok.Getter;

import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleSocketClient extends SimpleSocket {

    private boolean connected;

    private String host;
    private int port;

    @Getter
    private Socket socket;
    private SimpleSocketHandler simpleSocketHandler;

    private ScheduledExecutorService executor;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public SimpleSocketClient() {

    }

    public void connect(String host, int port) throws Exception {
        this.host = host;
        this.port = port;

        this.socket = new Socket(host, port);
        this.simpleSocketHandler = new SimpleSocketHandler(this, this.socket);

        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.schedule(() -> {
            //Haven't received a 0 in 3 minutes that tells that this socket is no longer connected to the server socket
            if (atomicInteger.get() == 0) {
                this.connected = false;
                System.out.println("Noticed that socket has been disconnected. Trying to reconnect!");
                this.simpleSocketHandler.close();
                //Let's try to connect again
                try {
                    this.socket = new Socket(this.host, this.port);
                    this.simpleSocketHandler = new SimpleSocketHandler(this, this.socket);
                    this.connected = true; //We are connected once again!
                } catch (Exception e) {
                    //We tried to reconnect yet no success so just stop
                    stop();
                }
            } else {
                atomicInteger.set(0);
            }
        }, 3L, TimeUnit.MINUTES);
    }

    public void sendMessage(String message) {
        if (simpleSocketHandler.isOn()) {
            this.simpleSocketHandler.getOut().println(message);
        }
    }

    public void stop() {
        this.simpleSocketHandler.close();
        this.executor.shutdown();
    }

    @Override
    void receivedHeartbeat(SimpleSocketHandler simpleSocketHandler) {
        atomicInteger.getAndIncrement();
    }
}
