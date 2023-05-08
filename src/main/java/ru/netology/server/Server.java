package ru.netology.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Server {
    private final int PORT = 8080;
    private final String HOST = "localhost";


    public void start() {
        try {
            File settings = new File("settings.txt");
            if (settings.createNewFile() || settings.exists()) {
                //TODO: log
                recordSettings(HOST, PORT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: log
        }
    }

    private void recordSettings(String HOST, int PORT) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("settings.txt", false));
        writer.write("HOST " + HOST + "\n");
        writer.write("PORT " + PORT);
        writer.flush();
        writer.close();
        //TODO: log
    }
}
