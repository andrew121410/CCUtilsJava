package com.andrew121410.ccutils.network.nio;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;

public class AsyncSocketServer {

    private Map<String, AsyncServerRead> asyncServerReadMap;

    private String host;
    private int port;

    public AsyncSocketServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.asyncServerReadMap = new HashMap<>();
    }

    public void create() throws IOException {
        InetSocketAddress sockAddr = new InetSocketAddress(this.host, this.port);
        AsynchronousServerSocketChannel serverSock = AsynchronousServerSocketChannel.open().bind(sockAddr);
        serverSock.accept(serverSock, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
            @Override
            public void completed(AsynchronousSocketChannel sockChannel, AsynchronousServerSocketChannel serverSock) {
                serverSock.accept(serverSock, this);
                startRead(sockChannel);
            }

            @Override
            public void failed(Throwable exc, AsynchronousServerSocketChannel serverSock) {
                System.out.println("Failed to accept connection.");
            }
        });
    }

    private void startRead(AsynchronousSocketChannel sockChannel) {
        final ByteBuffer buf = ByteBuffer.allocate(4096);
        //read message from client
        sockChannel.read(buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel) {
                channel.read(buf);
                String message = new String(buf.array()).trim();
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = null;
                try {
                    jsonObject = (JSONObject) jsonParser.parse(message);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                AsyncServerRead asyncServerRead = asyncServerReadMap.get(jsonObject.get("Type"));
                if (asyncServerRead == null)
                    throw new NullPointerException("AsyncSocketServer : startRead -> asyncServerRead is null");
                asyncServerRead.onRead(channel, result, jsonObject);
                startRead(channel);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println("fail to read message from client");
            }
        });
    }

    public void registerReadHandler(String type, AsyncServerRead asyncServerRead) {
        this.asyncServerReadMap.putIfAbsent(type, asyncServerRead);
    }

    public static ByteBuffer from(String string) {
        return ByteBuffer.wrap(string.getBytes());
    }
}
