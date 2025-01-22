package ru.kpfu.itis.shcherbanev.roommulticlientchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final List<ClientWorker> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server started");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("connected");
                ClientWorker clientWorker = new ClientWorker(socket, clients);
                clients.add(clientWorker);
                clientWorker.start();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
