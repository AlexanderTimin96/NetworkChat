package ru.netology.client;

public class MainClient2 {
    public static void main(String[] args) {
        Client client = new Client("src/main/java/ru/netology/client/log/clientLog2.log");
        client.start();
    }
}
