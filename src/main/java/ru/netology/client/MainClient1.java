package ru.netology.client;

public class MainClient1 {
    public static void main(String[] args) {
        Client client = new Client("src/main/java/ru/netology/client/ClientLog1.log");
        client.start();
    }
}
