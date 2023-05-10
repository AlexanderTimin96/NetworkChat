package ru.netology.server;

import ru.netology.logger.LevelLog;
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
    public int PORT = 8080;
    public String HOST = "localhost";

    private final static Logger logger = Logger.getInstance();
    private final String pathNameLogFile = "src/main/java/ru/netology/server/serverLog.log";

    private static final String pathNameSettings = "src/main/java/ru/netology/server/settings.txt";

    private final List<ClientHandler> clientList = Collections.synchronizedList(new ArrayList<>());

    public void start() {
        try {
            logger.createFileLog(pathNameLogFile);
            File settings = new File(pathNameSettings);
            if (settings.createNewFile() || settings.exists()) {
                recordSettings(HOST, PORT);
                logger.log(pathNameLogFile, LevelLog.INFO, "Recording the setting is successful");
            }
        } catch (IOException e) {
            logger.log(pathNameLogFile, LevelLog.ERROR, "Error when creating settings file");
        }

        System.out.println("Server start...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.log(pathNameLogFile, LevelLog.INFO, "Server starting (" + PORT + "). Waiting for connections");
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientList, pathNameLogFile);
                logger.log(pathNameLogFile, LevelLog.INFO, "The new connection is redirected to the ClientHandler thread");
                clientList.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            logger.log(pathNameLogFile, LevelLog.ERROR, "Error with starting server(" + PORT + ")");
        } finally {
            for (ClientHandler clientHandler : clientList) {
                clientHandler.interrupt();
                logger.log(pathNameLogFile, LevelLog.INFO, "Interrupt all ClientHandler threads");
            }
        }
    }

    private void recordSettings(String HOST, int PORT) {
        try {
            FileWriter out = new FileWriter(pathNameSettings, false);
            BufferedWriter in = new BufferedWriter(out);
            in.write("HOST " + HOST + "\n");
            in.write("PORT " + PORT);
            in.flush();
            in.close();
            out.close();
        } catch (IOException e) {
            logger.log(pathNameLogFile, LevelLog.ERROR, "Error when writing settings");
        }
    }
}
