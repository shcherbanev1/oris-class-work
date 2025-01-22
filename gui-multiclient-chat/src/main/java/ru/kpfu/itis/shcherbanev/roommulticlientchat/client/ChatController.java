package ru.kpfu.itis.shcherbanev.roommulticlientchat.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatController {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    @FXML
    private TextArea chatArea;
    @FXML
    private TextField messageField;

    public void initialize() {
        name = askForName();
        if (name == null) {
            Platform.exit();
            return;
        }
        new Thread(this::connectToServer).start();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println(name);

            String message;
            while ((message = in.readLine()) != null) {
                chatArea.appendText(message + "\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            messageField.clear();
        }
    }

    private String askForName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Chat entry");
        dialog.setHeaderText("Input name");
        return dialog.showAndWait().orElse(null);
    }
}
