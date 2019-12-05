package CCUtils.Sockets.Sockets.Server;

import CCUtils.Sockets.interfaces.ServerSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class SimpleServerSocket {

    private Map<String, ServerSocketHandler> serverSocketHandlerMap;

    private ServerSocketManager serverSocketManager;

    private int port;

    public SimpleServerSocket(int port) {
        this.port = port;

        this.serverSocketHandlerMap = new HashMap<>();

        new Thread(new ServerSocketManager(this.port, this.serverSocketHandlerMap)).start();
    }

    public void registerHandler(String key, ServerSocketHandler serverSocketHandler) {
        this.serverSocketHandlerMap.putIfAbsent(key, serverSocketHandler);
    }

    public void deleteHandler(String key) {
        this.serverSocketHandlerMap.remove(key);
    }
}
