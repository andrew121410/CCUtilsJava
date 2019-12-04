package CCUtils.Sockets.Server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerSocketHandler implements Runnable {

    private Map<String, CCUtils.Sockets.Server.interfaces.ServerSocketHandler> serverSocketHandlerMap;

    private ThreadPoolExecutor threadPoolExecutor;

    private PrintWriter out;
    private Scanner inSc;

    private Socket socket;

    private boolean isClosed;

    protected ServerSocketHandler(Socket socket, ThreadPoolExecutor threadPoolExecutor, Map<String, CCUtils.Sockets.Server.interfaces.ServerSocketHandler> serverSocketHandlerMap) {
        this.socket = socket;
        this.threadPoolExecutor = threadPoolExecutor;
        this.serverSocketHandlerMap = serverSocketHandlerMap;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            inSc = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            isClosed = true;
            close();
            e.printStackTrace();
            return;
        }
        isClosed = false;
    }

    @Override
    public void run() {
        while (!isClosed && inSc.hasNextLine()) {
            String line = inSc.nextLine();

            JSONParser parser = new JSONParser();
            JSONObject json;
            try {
                json = (JSONObject) parser.parse(line);
            } catch (ParseException e) {
                System.out.println("NOT JSON");
                return;
            }

            if (json == null) return;

            String who = (String) json.get("WHO");
            CCUtils.Sockets.Server.interfaces.ServerSocketHandler serverSocketHandler = this.serverSocketHandlerMap.get(who);
            if (serverSocketHandler != null) {
                serverSocketHandler.translate(json);
            } else System.out.println("ClientHandler -> WHO: " + who);

            Boolean waitForAResponse = (Boolean) json.get("WFR");
            if (waitForAResponse == null || !waitForAResponse) {
                isClosed = true;
                close();
            }
        }
    }

    private void close() {
        try {
            this.out.close();
            this.inSc.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.threadPoolExecutor.remove(this);
            System.out.println("Task has been removed from Thread pool");
            System.out.println("SOCKET has been closed." + socket);
        }
    }
}
