package ru.netology.server;

import ru.netology.logger.LevelLog;
import ru.netology.logger.Logger;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientHandler extends Thread {
    Socket socket;
    public BufferedWriter out;
    private BufferedReader in;
    private final List<ClientHandler> clientList;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private final Logger logger = Logger.getInstance();
    private final String pathNameLogFile;

    public ClientHandler(Socket socket, List<ClientHandler> clientList, String pathNameLogFile) {
        this.socket = socket;
        this.clientList = clientList;
        this.pathNameLogFile = pathNameLogFile;
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            logger.log(pathNameLogFile, LevelLog.ERROR, "Error in constructor ClientHandler");
        }
    }

    @Override
    public void run() {
        try {
            String name = in.readLine();
            logger.log(pathNameLogFile, LevelLog.INFO, "Name {" + name + "} received from client");
            setName(name);
            System.out.println("The client(" + name + " is connected");
            logger.log(pathNameLogFile, LevelLog.INFO, "Setting the client name");
            sendMsg("You connecting to NetworkChat with name: " + name + ". You can send messages:");
            sendMsgToOtherClients(name + " joined the chat!");

            while (true) {
                String msg = in.readLine();
                if ("/exit".equals(msg)) {
                    logger.log(pathNameLogFile, LevelLog.INFO, "The client(" + getName() + "exited the chat. Closing the ClientHandler thread");
                    sendMsgToOtherClients(" left the chat!");
                    out.close();
                    in.close();
                    clientList.remove(this);
                    break;
                }
                sendMsgToOtherClients(msg);
            }
            interrupt();
        } catch (IOException e) {
            logger.log(pathNameLogFile, LevelLog.ERROR, "Error when reading a message from a client("
                    + getName() + ")");
        }
    }

    public void sendMsg(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
            logger.log(pathNameLogFile, LevelLog.INFO, "Response to the client("
                    + this.getName() + "): " + msg);
        } catch (IOException e) {
            logger.log(pathNameLogFile, LevelLog.ERROR, "Error when sending response a message from a client("
                    + getName() + ")");
        }
    }

    private void sendMsgToOtherClients(String msg) {
        for (ClientHandler clientHandler : clientList) {
            try {
                if (!clientHandler.getName().equals(this.getName())) {
                    clientHandler.out.write("[" + dtf.format(LocalDateTime.now()) + "] " + getName() + ": "
                            + msg + "\n");
                    clientHandler.out.flush();
                    logger.log(pathNameLogFile, LevelLog.INFO, "Send to the client("
                            + clientHandler.getName() + "): " + msg);
                }
            } catch (IOException e) {
                logger.log(pathNameLogFile, LevelLog.ERROR, "Error when sending a message to all clients");
            }
        }
    }
}
