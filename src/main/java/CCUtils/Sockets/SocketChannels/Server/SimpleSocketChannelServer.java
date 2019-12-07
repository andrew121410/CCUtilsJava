package CCUtils.Sockets.SocketChannels.Server;

import CCUtils.Sockets.interfaces.ServerSocketHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
            serverSocketChannel.socket().bind(new InetSocketAddress(port));

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keys = selectionKeys.iterator();

                while (keys.hasNext()) {
                    SelectionKey selectionKey = keys.next();

                    if (selectionKey.isAcceptable()) {
                        createChannel(serverSocketChannel, selectionKey);
                    } else if (selectionKey.isReadable()) {
                        doRead(selectionKey);
                    } else if (selectionKey.isWritable()) {
                        //TODO add write.
                    }
                    keys.remove();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createChannel(ServerSocketChannel serverSocketChannel, SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ); // selector pointing to READ operation
    }

    private void doRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(byteBuffer);
        if (read == -1) {
            doClose(socketChannel);
        } else {
            String result = new String(byteBuffer.array()).trim();

            JSONParser parser = new JSONParser();
            JSONObject json;
            try {
                json = (JSONObject) parser.parse(result);
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

            selectionKey.interestOps(SelectionKey.OP_WRITE); // set mode to WRITE to send data
        }
    }

    private void doClose(SocketChannel socketChannel) throws IOException {
        SocketAddress remoteSocketAddress = socketChannel.getRemoteAddress();
        System.out.println("Connection closed by client: " + remoteSocketAddress);
        socketChannel.close();
    }

    public void registerHandler(String key, ServerSocketHandler serverSocketHandler) {
        this.serverSocketHandlerMap.putIfAbsent(key, serverSocketHandler);
    }

    public void deleteHandler(String key) {
        this.serverSocketHandlerMap.remove(key);
    }
}