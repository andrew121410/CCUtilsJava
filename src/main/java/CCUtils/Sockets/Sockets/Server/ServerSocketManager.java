package CCUtils.Sockets.Sockets.Server;

import CCUtils.Sockets.interfaces.ServerSocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerSocketManager implements Runnable {

    private Map<String, ServerSocketHandler> serverSocketHandlerMap;

    private ServerSocket serverSocket;
    private ThreadPoolExecutor threadPool;

    private int port;

    protected ServerSocketManager(int port, Map<String, ServerSocketHandler> serverSocketHandlerMap) {
        this.port = port;
        this.serverSocketHandlerMap = serverSocketHandlerMap;
        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            close(serverSocket);
            e.printStackTrace();
        }
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();

                if (socket == null) return;

                System.out.println("New Client: " + socket);
                System.out.println("Adding to Thread Pool.");

                this.threadPool.execute(new CCUtils.Sockets.Sockets.Server.ServerSocketHandler(socket, threadPool, serverSocketHandlerMap));
            } catch (Exception e) {
                close(socket);
                e.printStackTrace();
            }
        }
    }

    private void close(Socket socket) {
        if (socket == null) return;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("SOCKET has been closed." + socket);
        }
    }

    private void close(ServerSocket socket) {
        if (socket == null) return;
        try {
            this.threadPool.shutdown();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("SOCKET has been closed." + socket);
        }
    }
}
