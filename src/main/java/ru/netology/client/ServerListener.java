package ru.netology.client;

import ru.netology.logger.LevelLog;
import ru.netology.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerListener extends Thread {
    private final String pathNamLogFile;
    private final BufferedReader in;
    private final Logger logger = Logger.getInstance();

    public ServerListener(BufferedReader in, String pathNameLogFile) {
        this.pathNamLogFile = pathNameLogFile;
        this.in = in;
        start();
        logger.log(pathNamLogFile, LevelLog.INFO, "Starting the Serverlistener thread");
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                String responseServer = in.readLine();
                if (responseServer != null) {
                    System.out.println(responseServer);
                    logger.log(pathNamLogFile, LevelLog.INFO, "Response {" + responseServer
                            + "} was accepted from the server and output to the console");
                }
            }
        } catch (IOException e) {
            logger.log(pathNamLogFile, LevelLog.ERROR, "Error when accepting response from server");
        }
    }
}
