package ru.netology.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ClientHandler extends Thread {

    Socket socket;
    public BufferedWriter out;
    private BufferedReader in;

    private String name;
    private final List<ClientHandler> clientList;

    public ClientHandler(Socket socket, List<ClientHandler> clientList) {
        this.socket = socket;
        this.clientList = clientList;
        start();
    }

    @Override
    public void run() {
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            name = in.readLine();
            out.write("You connecting to NetworkChat with name: " + name +  ". You can send messages:\n");
            out.flush();

            while (true) {
                String msg = in.readLine();
                if ((name + ": left the chat!").equals(msg)) {
                    sendMsg(msg);
                    out.close();
                    in.close();
                    break;
                }
                sendMsg(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String msg) {
        for (ClientHandler clientHandler : clientList) {
            try {
               if (!name.equals(this.getName())) {
                clientHandler.out.write(msg + "\n");
                clientHandler.out.flush();
               }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
