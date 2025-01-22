package GameObjects;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

import static Game.SpaceShooter.getTime;
import static Game.SpaceShooter.getUpdateRate;

public class game {

    private static player player1;
    private static player player2;
    private static ArrayList<String> input;

    private static double dimX;
    private static double dimY;

    private static Scene gameScene;
    private static Pane gameBox;
    private static Label debugLabel;

    private static int score = 0;
    private static Label scoreLabel;

    public game(double x, double y) {
        player1 = new player(0, 0);
        player2 = new player(0, y - 100);
        input = new ArrayList<>();
        dimX = x;
        dimY = y;

        BorderPane root = new BorderPane();
        gameBox = new Pane();
        HBox optionBox = new HBox();
        optionBox.setStyle("-fx-background-color: #ffffff;");
        debugLabel = new Label();

        Image back = new Image("/images/Background.png", x, y, false, false);
        ImageView background = new ImageView(back);
        background.setX(0);
        background.setY(0);

        gameBox.getChildren().add(background);
        gameBox.getChildren().addAll(player1);
        gameBox.getChildren().addAll(player1.getCANNON());

        gameBox.getChildren().addAll(player2);
        gameBox.getChildren().addAll(player2.getCANNON());

        spawnEnemy(400, 250);
        spawnEnemy(550, 250);

        Label debug = getDebugLabel();
        optionBox.getChildren().add(debug);

        gameScene = new Scene(root);

        scoreLabel = new Label("Score: " + score);
        scoreLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");

        scoreLabel.setTranslateX(dimX - 120);
        scoreLabel.setTranslateY(dimY - 40);

        gameBox.getChildren().add(scoreLabel);

//        graphicsContext = canvas.getGraphicsContext2D();
//
//
//        MediaPlayer music = new MediaPlayer(new Media(getClass().getResource("/sounds/steam_monster_summer_game.mp3").toString()));
//        music.setOnEndOfMedia(() -> music.seek(Duration.ZERO));
//        music.setVolume(0.1);
//        music.play();
//
//        VBox musicBox = new VBox();
//        Label musicLabel = new Label("Music volume");
//
//        Slider musicVolumeSlider = new Slider(0,0.5,0.1);
//        musicVolumeSlider.setMaxWidth(200);
//        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            music.setVolume((Double) newValue);
//        });
//
//        musicBox.getChildren().addAll(musicLabel,musicVolumeSlider);
//        optionBox.getChildren().add(musicBox);

        gameBox.setOnMouseClicked(this::mouseClick);

        gameScene.setOnKeyPressed(event -> {
            String keyInput = event.getCode().toString();
            System.out.println(keyInput + " was pressed!");
            if (!input.contains(keyInput)) {
                input.add(keyInput);
            }
        });

        gameScene.setOnKeyReleased(event -> {
            String keyInput = event.getCode().toString();
            input.remove(keyInput);
        });

        gameBox.setOnMouseDragged(event -> {
            player1.rotateCannon(event);
            player2.rotateCannon(event);
        });
        gameBox.setOnMouseMoved(event -> {
            player1.rotateCannon(event);
            player2.rotateCannon(event);
        });

        gameBox.setOnMousePressed(event -> {
//            player1.rotateCannon(event);

            if (event.getButton().toString().equals("PRIMARY")) {
                player1.startFiring();
                player2.startFiring();
            }
        });

        gameBox.setOnMouseReleased(event -> {
            player1.stopFiring();
            player2.stopFiring();
        });

        gameScene.setOnKeyPressed(event -> {
            String keyInput = event.getCode().toString();
            System.out.println(keyInput + " was pressed!");

            if (!input.contains(keyInput)) {
                input.add(keyInput);
            }

            switch (keyInput) {
                case "DIGIT1":
                    player1.setSpeedCoefficient(1);
                    player2.setSpeedCoefficient(1);
                    break;
                case "DIGIT2":
                    player1.setSpeedCoefficient(4);
                    player2.setSpeedCoefficient(4);
                    break;
                case "DIGIT3":
                    player1.setSpeedCoefficient(10);
                    player2.setSpeedCoefficient(10);
                    break;
            }
        });

        root.setCenter(gameBox);
        root.setTop(optionBox);
    }

    public Scene getGameScene() {
        return gameScene;
    }

//    private void updateGameScene(){
//        BorderPane root = new BorderPane();
//        gameScene.setRoot(root);
//        gameBox.getChildren().clear();
//        gameBox.getChildren().add(background);
//        gameBox.getChildren().addAll(asteroids);
//        gameBox.getChildren().addAll(player1);
//        gameBox.getChildren().addAll(player1.getCANNON());
//        root.setCenter(gameBox);
//        root.setTop(optionBox);
//        updateScene();
//    }

    public void moveGameObjects() {
        moveCharacter();
        debugLabel.setText(getDebugInfo());
    }

    private void moveCharacter() {
        boolean w = input.contains("W");
        boolean a = input.contains("A");
        boolean s = input.contains("S");
        boolean d = input.contains("D");
        boolean arrowUp = input.contains("UP");
        boolean arrowDown = input.contains("DOWN");
        boolean arrowRight = input.contains("RIGHT");
        boolean arrowLeft = input.contains("LEFT");
        boolean ctrl = input.contains("CONTROL");
        boolean shift = input.contains("SHIFT");

        if (w) {
            if (!ctrl) {
                player1.addVelocity(0, -0.01);
            } else {
                player1.addVelocity(0, -0.001);
            }
        }
        if (a) {
            if (!ctrl) {
                player1.addVelocity(-0.01, 0);
            } else {
                player1.addVelocity(-0.001, 0);
            }
        }
        if (s) {
            if (!ctrl) {
                player1.addVelocity(0, 0.01);
            } else {
                player1.addVelocity(0, 0.001);
            }
        }
        if (d) {
            if (!ctrl) {
                player1.addVelocity(0.01, 0);
            } else {
                player1.addVelocity(0.001, 0);
            }
        }

        if (arrowUp) {
            if (!shift) {
                player2.addVelocity(0, -0.01);
            } else {
                player2.addVelocity(0, -0.001);
            }
        }
        if (arrowLeft) {
            if (!shift) {
                player2.addVelocity(-0.01, 0);
            } else {
                player2.addVelocity(-0.001, 0);
            }
        }
        if (arrowDown) {
            if (!shift) {
                player2.addVelocity(0, 0.01);
            } else {
                player2.addVelocity(0, 0.001);
            }
        }
        if (arrowRight) {
            if (!shift) {
                player2.addVelocity(0.01, 0);
            } else {
                player2.addVelocity(0.001, 0);
            }
        }

        player1.update((double) 1000 / getUpdateRate(), gameBox);
        player2.update((double) 1000 / getUpdateRate(), gameBox);

        addFlameEffects(w, a, s, d, ctrl, player1);
        addFlameEffects(arrowUp, arrowLeft, arrowDown, arrowRight, shift, player2);
//        addFlameEffectUP(w,ctrl);
//        addFlameEffectLeft(a,ctrl);
//        addFlameEffectDOWN(s,ctrl);
//        addFlameEffectRIGHT(d,ctrl);

        player1.rotateCannon();
        player2.rotateCannon();

        ObservableList<Node> list = gameBox.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Node nd = list.get(i);

            if (nd instanceof Enemy) {
                Enemy enemy = (Enemy) nd;
                ObservableList<Node> projectiles = gameBox.getChildren();

                for (int j = 0; j < projectiles.size(); j++) {
                    Node proj = projectiles.get(j);

                    if (proj instanceof projectile) {
                        projectile bullet = (projectile) proj;

                        if (enemy.intersects(bullet)) {
                            list.remove(bullet);
                            score += 15;
                            updateScoreLabel();
                            if (enemy.getHit()) {
                                list.remove(enemy);
                            }
                        }
                    }
                }

//                if(ast.intersects(player1)){
//                    list.remove(ast);
//                    break;
//                }
            }
            if (nd instanceof projectile) {
                projectile pro = (projectile) nd;
                if (!pro.isOutOfBounds()) {
                    pro.update((double) 1000 / getUpdateRate());
                } else {
                    list.remove(pro);
                }
            }
        }
    }

//    public static player1 getPlayer() {
//        return player1;
//    }

    private static void spawnEnemy(double x, double y) {
        Enemy ast = new Enemy(x, y);
        ast.setPosition(x - ast.getWidth() / 2, y - ast.getHeight() / 2);
        gameBox.getChildren().add(ast);
    }

    private void mouseClick(MouseEvent event) {
        System.out.println("game was clicked at: " + event.getX() + "," + event.getY() + " (" + event.getButton() + ")");
        switch (event.getButton()) {
            case PRIMARY:
//                double deltaX = event.getX() - player1.getCenterX();
//                double deltaY = event.getY() - player1.getCenterY();
//                spawnProjectile(player1.getCenterX(),player1.getCenterY(),deltaX, deltaY);
//              do left getHit

                break;
            case SECONDARY:

                spawnEnemy(event.getX(), event.getY());
//                do right getHit

                break;
            case MIDDLE:

//                do middle getHit

                break;
            case NONE:
                break;
        }
    }

    private void addFlameEffects(boolean w, boolean a, boolean s, boolean d, boolean ctrl, player pl) {
        addFlameEffectUP(w, ctrl, pl);
        addFlameEffectLeft(a, ctrl, pl);
        addFlameEffectDOWN(s, ctrl, pl);
        addFlameEffectRIGHT(d, ctrl, pl);
    }

    private void addFlameEffectUP(boolean on, boolean ctrl, player pl) {
        if (on) {

            if (!ctrl) {
                if (!gameBox.getChildren().contains(pl.getUP())) {
                    gameBox.getChildren().add(pl.getUP());
                    gameBox.getChildren().remove(pl.getUP_SMALL());
                }
            } else {
                if (!gameBox.getChildren().contains(pl.getUP_SMALL())) {
                    gameBox.getChildren().add(pl.getUP_SMALL());
                    gameBox.getChildren().remove(pl.getUP());
                }
            }
        } else {
            gameBox.getChildren().remove(pl.getUP());
            gameBox.getChildren().remove(pl.getUP_SMALL());
        }
//        if(on){
//
//            if (!ctrl){
//                if(player1.getUP().isVisible()){
//                    player1.getUP().setVisible(true);
//                    player1.getUP_SMALL().setVisible(false);
//                }
//            } else {
//                if(!player1.getUP_SMALL().isVisible()){
//                    player1.getUP_SMALL().setVisible(true);
//                    player1.getUP().setVisible(false);
//                }
//            }
//        } else {
//            player1.getUP().setVisible(false);
//            player1.getUP_SMALL().setVisible(false);
//        }
    }

    private void addFlameEffectLeft(boolean on, boolean ctrl, player pl) {
        if (on) {
            if (!ctrl) {
                if (!gameBox.getChildren().contains(pl.getLEFT())) {
                    gameBox.getChildren().add(pl.getLEFT());
                    gameBox.getChildren().remove(pl.getLEFT_SMALL());
                }
            } else {
                if (!gameBox.getChildren().contains(pl.getLEFT_SMALL())) {
                    gameBox.getChildren().add(pl.getLEFT_SMALL());
                    gameBox.getChildren().remove(pl.getLEFT());
                }
            }
        } else {
            gameBox.getChildren().remove(pl.getLEFT());
            gameBox.getChildren().remove(pl.getLEFT_SMALL());
        }
    }

    private void addFlameEffectDOWN(boolean on, boolean ctrl, player pl) {
        if (on) {
            if (!ctrl) {
                if (!gameBox.getChildren().contains(pl.getDOWN())) {
                    gameBox.getChildren().add(pl.getDOWN());
                    gameBox.getChildren().remove(pl.getDOWN_SMALL());
                }
            } else {
                if (!gameBox.getChildren().contains(pl.getDOWN_SMALL())) {
                    gameBox.getChildren().add(pl.getDOWN_SMALL());
                    gameBox.getChildren().remove(pl.getDOWN());
                }
            }
        } else {
            gameBox.getChildren().remove(pl.getDOWN());
            gameBox.getChildren().remove(pl.getDOWN_SMALL());
        }
    }

    private void addFlameEffectRIGHT(boolean on, boolean ctrl, player pl) {
        if (on) {
            if (!ctrl) {
                if (!gameBox.getChildren().contains(pl.getRIGHT())) {
                    gameBox.getChildren().add(pl.getRIGHT());
                    gameBox.getChildren().remove(pl.getRIGHT_SMALL());
                }
            } else {
                if (!gameBox.getChildren().contains(pl.getRIGHT_SMALL())) {
                    gameBox.getChildren().add(pl.getRIGHT_SMALL());
                    gameBox.getChildren().remove(pl.getRIGHT());
                }
            }
        } else {
            gameBox.getChildren().remove(pl.getRIGHT());
            gameBox.getChildren().remove(pl.getRIGHT_SMALL());
        }
    }

    static void spawnProjectile(double x, double y, double mousex, double mousey) {
        gameBox.getChildren().add(new projectile(x, y, mousex, mousey));
    }

    static double getGameDimX() {
        return dimX;
    }

    static double getGameDimY() {
        return dimY;
    }

    private Label getDebugLabel() {
        debugLabel.setText(getDebugInfo());
        debugLabel.setMinHeight(40);
        return debugLabel;
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

    private String getDebugInfo() {
        return "Time: " + getTime() / getUpdateRate() + ", Position: " + (short) player1.getPositionX() + " (" + (short) (player1.getPositionX()
                                                                                                                          + player1.getWidth()) + "), " + (short) player1.getPositionY() + " (" + (short) (player1.getPositionY() + player1.getHeight()) + ")" + "Game objects: " + gameBox.getChildren().size()
               + "\nwidth: " + player1.getWidth() + ", height: " + player1.getHeight()
               + ", Velocity: " + player1.getVelocityX() + "," + player1.getVelocityY();
    }
}