package ru.netology.client;

import ru.netology.logger.LevelLog;
import ru.netology.logger.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String HOST;
    private int PORT;

    private final String name;

    private static final String pathNameSettings = "src/main/java/ru/netology/server/settings.txt";
    private final String pathNameLogFile;

    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger = Logger.getInstance();


    public Client(String pathNameLogFile) {
        this.pathNameLogFile = pathNameLogFile;
        logger.createFileLog(pathNameLogFile);

        readSettings(pathNameSettings);

        System.out.println("Write you nickname(nickname must consist of three or more characters):");
        String inputName;
        while (true) {
            logger.log(this.pathNameLogFile, LevelLog.INFO, "The client is asked to enter a name");
            inputName = scanner.nextLine();
            if (inputName.trim().length() < 2) {
                System.out.println("Invalid nickname! Re-enter:");
                logger.log(this.pathNameLogFile, LevelLog.INFO, "The client entered an invalid name");
            } else {
                name = inputName;
                logger.log(this.pathNameLogFile, LevelLog.INFO, "The client has set the name " + name);
                break;
            }
        }
    }

    public void start() {
        try (Socket clientSocket = new Socket(HOST, PORT);
             BufferedWriter out = new BufferedWriter(new PrintWriter(clientSocket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            logger.log(pathNameLogFile, LevelLog.INFO, "The client has connected with server (PORT: "
                    + PORT + "HOST: " + HOST + ")");
            try {
                out.write(name + "\n");
                out.flush();
                logger.log(pathNameLogFile, LevelLog.INFO, "Send client's name to server");
            } catch (IOException e) {
                logger.log(pathNameLogFile, LevelLog.ERROR, "Error when sending client's name to server");
            }

            ServerListener serverListener = new ServerListener(in, pathNameLogFile);

            while (true) {
                String msg = scanner.nextLine();
                if ("".equals(msg)) {
                    continue;
                }
                logger.log(pathNameLogFile, LevelLog.INFO, "Reading message in console");
                try {
                    if (msg.equals("/exit")) {
                        out.write("/exit\n");
                        out.flush();
                        logger.log(pathNameLogFile, LevelLog.INFO, "Send {" + msg + "} to server. Exiting the chat");
                        break;
                    }
                    out.write(msg + "\n");
                    out.flush();
                    logger.log(pathNameLogFile, LevelLog.INFO, "Send {" + msg + "} to server");
                } catch (IOException e) {
                    logger.log(pathNameLogFile, LevelLog.ERROR, "Error when sending message to server");
                }
            }

            serverListener.interrupt();
            in.close();
            scanner.close();
        } catch (IOException e) {
            logger.log(pathNameLogFile, LevelLog.ERROR, "Error with connection to server (PORT: "
                    + PORT + ", HOST: " + HOST + ")");
        }
    }

    private void readSettings(String pathNameSettings) {
        try (BufferedReader in = new BufferedReader(new FileReader(pathNameSettings))) {
            logger.log(pathNameLogFile, LevelLog.INFO, "Reading the settings...");
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("PORT")) {
                    String[] path = line.split(" ");
                    PORT = Integer.parseInt(path[1]);
                    logger.log(pathNameLogFile, LevelLog.INFO, "PORT installed to " + PORT);
                } else if (line.contains("HOST")) {
                    HOST = line.split(" ")[1];
                    logger.log(pathNameLogFile, LevelLog.INFO, "HOST installed to " + HOST);
                }
            }
            logger.log(pathNameLogFile, LevelLog.INFO, "Reading the settings is successful");
        } catch (IOException e) {
            logger.log(pathNameLogFile, LevelLog.ERROR, "Error when reading the settings");
        } catch (NumberFormatException e) {
            logger.log(pathNameLogFile, LevelLog.ERROR, "Error when when parsing the PORT");
        }
    }
}

