package ru.netology.server;

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

    public ClientHandler(Socket socket, List<ClientHandler> clientList) {
        this.socket = socket;
        this.clientList = clientList;
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        try {
            String name = in.readLine();
            this.setName(name);
            sendMsg("You connecting to NetworkChat with name: " + name + ". You can send messages:\n");
            sendMsgToOtherClients(name + " joined the chat!");

            while (true) {
                String msg = in.readLine();
                if ("/exit".equals(msg)) {
                    sendMsg("You left the chat!");
                    sendMsgToOtherClients(name + ": left the chat!");
                    out.close();
                    in.close();
                    this.interrupt();
                    break;
                }
                sendMsgToOtherClients(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) throws IOException {
        out.write(msg);
        out.flush();
    }

    private void sendMsgToOtherClients(String msg) {
        for (ClientHandler clientHandler : clientList) {
            try {
                if (!clientHandler.getName().equals(this.getName())) {
                    clientHandler.out.write( "[" + dtf.format(LocalDateTime.now()) + "] "+ getName() + ": "
                            + msg + "\n");
                    clientHandler.out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
