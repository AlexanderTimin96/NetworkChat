package ru.netology.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Client {
    private String HOST;
    private int PORT;
    private String name;

    public Client() {
        try (BufferedReader in = new BufferedReader(new FileReader("settings.txt"))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("PORT")) {
                    String[] path = line.split(" ");
                    PORT = Integer.parseInt(path[1]);
                } else if (line.contains("HOST")) {
                    HOST = line.split(" ")[1];
                }
            }
            //TODO: log
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: log
        } catch (NumberFormatException e) {
            e.printStackTrace();
            //TODO: log
        }
    }

    public void start() {

    }
}
