package ru.netology.client;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerListener extends Thread {
    BufferedReader in;

    public ServerListener(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String responseServer = in.readLine();
                System.out.println(responseServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
