package CCUtils.Sockets.SocketChannels.Server;

import CCUtils.Sockets.interfaces.ServerSocketHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SimpleSocketChannelServer {

    private Map<String, ServerSocketHandler> serverSocketHandlerMap;

    public SimpleSocketChannelServer(int port) {
        this.serverSocketHandlerMap = new HashMap<>();

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", port));

            Selector selector = Selector.open();
            int ops = serverSocketChannel.validOps();
            serverSocketChannel.register(selector, ops, null);

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("Connection Accepted: " + socketChannel.getLocalAddress() + "\n");

                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                        socketChannel.read(byteBuffer);
                        String message = new String(byteBuffer.array()).trim();

                        JSONParser parser = new JSONParser();
                        JSONObject json;
                        try {
                            json = (JSONObject) parser.parse(message);
                        } catch (ParseException e) {
                            System.out.println("NOT JSON");
                            return;
                        }
                        if (json == null) return;

                        String who = (String) json.get("WHO");
                        CCUtils.Sockets.interfaces.ServerSocketHandler serverSocketHandler = this.serverSocketHandlerMap.get(who);
                        if (serverSocketHandler != null) {
                            serverSocketHandler.translate(json);
                        } else System.out.println("ClientHandler -> WHO: " + who);
                    }

                    iterator.remove();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void registerHandler(String key, ServerSocketHandler serverSocketHandler) {
        this.serverSocketHandlerMap.putIfAbsent(key, serverSocketHandler);
    }

    public void deleteHandler(String key) {
        this.serverSocketHandlerMap.remove(key);
    }
}