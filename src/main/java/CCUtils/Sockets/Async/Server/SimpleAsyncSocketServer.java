package CCUtils.Sockets.Async.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleAsyncSocketServer {

    private AsynchronousServerSocketChannel server;

    private String host;
    private int port;

    public SimpleAsyncSocketServer(String host, int port) {
        this.host = host;
        this.port = port;

        setup();
    }

    private void setup() {
        try {
            server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(this.host, this.port));
            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {

                @Override
                public void completed(AsynchronousSocketChannel asynchronousSocketChannel, Void aVoid) {

                    ByteBuffer b
                    asynchronousSocketChannel.read()

                    server.accept(null, this); //Start listening again.
                }

                @Override
                public void failed(Throwable reason, Void aVoid) {
                    throw new RuntimeException(reason);
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}