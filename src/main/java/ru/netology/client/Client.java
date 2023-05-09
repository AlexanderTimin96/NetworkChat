package ru.netology.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Client {
    private String HOST;
    private int PORT;
    private final String name;
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

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

        System.out.println("Write you nickname(nickname must consist of three or more characters):");
        String inputName;
        while (true) {
            inputName = scanner.nextLine();
            if (inputName.trim().length() < 2) {
                System.out.println("Invalid nickname! Re-enter:");
            } else {
                name = inputName;
                break;
            }
        }
    }

    public void start() {
        try (Socket clientSocket = new Socket(HOST, PORT);
            BufferedWriter out = new BufferedWriter (new PrintWriter(clientSocket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.write(name + "\n");
            out.flush();

            ServerListener serverListener = new ServerListener(in);
            serverListener.start();

            while (true) {
                String msg = scanner.nextLine();
                if (msg.equals("/exit")) {
                    out.write(name + ": left the chat!\n");
                    out.flush();
                    break;
                }
                out.write("[" + dtf.format(LocalDateTime.now()) + "] " + name + ": " + msg + "\n");
                out.flush();
            }

            serverListener.interrupt();
            scanner.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
