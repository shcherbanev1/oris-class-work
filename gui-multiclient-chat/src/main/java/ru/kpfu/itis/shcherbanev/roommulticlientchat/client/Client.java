package ru.kpfu.itis.shcherbanev.roommulticlientchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private Thread readerThread;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234)) {
            System.out.println("connected");
            Client client = new Client(socket);

            Thread reader = new Thread(client);

            System.out.println("Input your name");
            String name = client.scanner.nextLine();
            client.out.println(name);

            reader.start();

            while (client.scanner.hasNextLine()) {
                client.out.println(client.scanner.nextLine());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        String message;
        while (true) {
            try {
                if ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
