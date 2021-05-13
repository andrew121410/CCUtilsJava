package com.andrew121410.ccutils.sockets;

import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleSocketServer extends SimpleSocket {

    private List<SimpleSocketHandler> simpleSocketHandlers;

    private ScheduledExecutorService executor;
    @Getter
    private Map<SimpleSocketHandler, AtomicInteger> waitingMap;

    @Getter
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.simpleSocketHandlers = new ArrayList<>();
        theLoop();

        //Reconnecting Manager
        this.waitingMap = new HashMap<>();
        this.executor = Executors.newScheduledThreadPool(1);

        executor.schedule(() -> {
            Iterator<Map.Entry<SimpleSocketHandler, AtomicInteger>> iterator = waitingMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SimpleSocketHandler, AtomicInteger> entry = iterator.next();
                SimpleSocketHandler simpleSocketHandler = entry.getKey();
                AtomicInteger atomicInteger = entry.getValue();

                //Tried to contact the client socket 2 times and no response so we close the connection
                if (atomicInteger.get() == 3) {
                    simpleSocketHandler.close();
                }

                //Try to see if the socket is still connected by sending a message and waiting for a response
                if (simpleSocketHandler.isOn()) {
                    atomicInteger.getAndIncrement();
                    simpleSocketHandler.getOut().println("0");
                } else {
                    iterator.remove();
                }
            }
        }, 1L, TimeUnit.MINUTES);
    }

    private void theLoop() {
        //Yes I only really not smart but for now it works and I will fix later
        new Thread(() -> {
            while (true) {
                try {
                    simpleSocketHandlers.add(new SimpleSocketHandler(this, serverSocket.accept()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void stop() {
        try {
            this.executor.shutdown();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void receivedHeartbeat(SimpleSocketHandler simpleSocketHandler) {
        this.waitingMap.computeIfPresent(simpleSocketHandler, (simpleSocketHandler1, atomicInteger) -> {
            atomicInteger.set(0);
            return atomicInteger;
        });
    }
}