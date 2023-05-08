package ru.netology.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    Socket socket;
    String name;
    BufferedWriter out;
    BufferedReader in;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                name = in.readLine();
                String msg;
                while (true) {
                    msg = in.readLine();
                    if ("/exit".equals(msg)) {
                        out.close();
                        in.close();
                        break;
                    }
                }
                out.write(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
