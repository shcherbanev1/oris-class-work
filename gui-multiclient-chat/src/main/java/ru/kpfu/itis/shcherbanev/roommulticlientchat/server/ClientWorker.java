package ru.kpfu.itis.shcherbanev.roommulticlientchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientWorker implements Runnable {

    private List<ClientWorker> clients;
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Thread thread;
    private String name;

    public ClientWorker(Socket socket, List<ClientWorker> clients) throws IOException {
        this.socket = socket;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            name = in.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        String message;
        while (true) {
            try {
                if ((message = in.readLine()) != null) {
                    sendMessage(message, this);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void sendMessage(String message, ClientWorker sender) {
        for (ClientWorker client : clients) {
            if (client == sender) {
                client.out.println("You: " + message);
            } else {
                client.out.println(sender.name +  ": " + message);
            }
        }
    }
}
