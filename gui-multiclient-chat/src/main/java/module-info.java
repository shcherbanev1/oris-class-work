module ru.kpfu.itis.shcherbanev.roommulticlientchat {
    requires javafx.controls;
    requires javafx.fxml;

    exports ru.kpfu.itis.shcherbanev.roommulticlientchat.client;
    opens ru.kpfu.itis.shcherbanev.roommulticlientchat.client to javafx.fxml;
}