package ru.netology.server;

import ru.netology.logger.Logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private final static Logger logger = Logger.getInstance();
    private final List<ClientHandler> clientList = Collections.synchronizedList(new ArrayList<>());

    public void start() {
        int PORT = 8080;
        String HOST = "localhost";

        try {
            File settings = new File("src/main/java/ru/netology/server/settings.txt");
            if (settings.createNewFile() || settings.exists()) {
                recordSettings(HOST, PORT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server start...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientList);
                clientList.add(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: log
        } finally {
            for (ClientHandler clientHandler : clientList) {
                clientHandler.interrupt();
            }
        }
    }

    private void recordSettings(String HOST, int PORT) throws IOException {
        FileWriter out = new FileWriter("src/main/java/ru/netology/server/settings.txt", false);
        BufferedWriter in = new BufferedWriter(out);
        in.write("HOST " + HOST + "\n");
        in.write("PORT " + PORT);
        in.flush();
        in.close();
        out.close();
        //TODO: log
    }
}
