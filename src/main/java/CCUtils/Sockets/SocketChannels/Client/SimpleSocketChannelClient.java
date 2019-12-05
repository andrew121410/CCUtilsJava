package CCUtils.Sockets.SocketChannels.Client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SimpleSocketChannelClient {

    private SocketChannel socketChannel;

    private String host;
    private int port;

    public SimpleSocketChannelClient(String host, int port) {
        this.host = host;
        this.port = port;

        setup();
    }

    private void setup() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(this.host, this.port));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        run();
    }

    public void jsonPrintOut(JSONObject jsonObject, String who, boolean waitForAResponse) {
        if (!socketChannel.isConnected()) return;
        jsonObject.put("WHO", who);
        jsonObject.put("WFR", waitForAResponse);

        try {
            byte[] message = jsonObject.toJSONString().getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            socketChannel.write(buffer);
            buffer.clear();
            if (!waitForAResponse) {
                socketChannel.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void run() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

            while (true) {
                while (socketChannel.read(byteBuffer) > 0) {
                    String value = new String(byteBuffer.array(), 0, byteBuffer.limit());
                    JSONParser parser = new JSONParser();
                    JSONObject json;
                    json = (JSONObject) parser.parse(value);
                    if (json == null) return;

                    translate(json);
                    byteBuffer.clear();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void translate(JSONObject jsonObject) {

    }
}
