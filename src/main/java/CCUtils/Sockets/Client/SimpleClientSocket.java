package CCUtils.Sockets.Client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClientSocket {

    private String host;
    private int port;

    private PrintWriter out;
    private BufferedReader in;
    private Scanner inSc;

    private Socket socket;

    public SimpleClientSocket(String host, int port) {
        this.host = host;
        this.port = port;
        setup();
    }

    private void setup() {
        try {
            socket = new Socket(this.host, this.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inSc = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        run();
    }

    public void jsonPrintOut(JSONObject jsonObject, String who, boolean waitForAResponse) {
        if (socket.isClosed()) setup();
        if (socket.isClosed()) return;
        jsonObject.put("WHO", who);
        jsonObject.put("WFR", waitForAResponse);
        out.println(jsonObject.toJSONString());
        if (!waitForAResponse) {
            close();
        }
    }

    private void close() {
        try {
            socket.close();
            out.close();
            in.close();
            inSc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        String line;
        try {
            while (in.ready() && (line = in.readLine()) != null) {
                JSONParser parser = new JSONParser();
                JSONObject json;
                json = (JSONObject) parser.parse(line);
                if (json == null) return;

                translate(json);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
    }

    public void translate(JSONObject jsonObject) {

    }
}