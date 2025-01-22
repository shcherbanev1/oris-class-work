package GameObjects;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import static GameObjects.game.*;

class player extends sprite {

    private AnimationTimer firing;
    private long lastUpdate;
    private double velocityX;
    private double velocityY;

    private double mouseX;
    private double mouseY;

    private static double damage = 15;
    private double firingSpeed = 60;

    private Image playerImage = new Image("/images/player/player.png");

    private ImageView UP;
    private ImageView UP_SMALL;
    private ImageView LEFT;
    private ImageView LEFT_SMALL;
    private ImageView DOWN;
    private ImageView DOWN_SMALL;
    private ImageView RIGHT;
    private ImageView RIGHT_SMALL;

    private ImageView CANNON;

    private int speedCoefficient = 1;

    player(double x, double y) {
        super(x, y);
        setImage(playerImage);

        velocityX = 0;
        velocityY = 0;
        mouseX = 0;
        mouseY = 0;

        UP = new ImageView("/images/player/UP.png");
        UP_SMALL = new ImageView("/images/player/UP_SMALL.png");

        LEFT = new ImageView("/images/player/LEFT.png");
        LEFT_SMALL = new ImageView("/images/player/LEFT_SMALL.png");

        DOWN = new ImageView("/images/player/DOWN.png");
        DOWN_SMALL = new ImageView("/images/player/DOWN_SMALL.png");

        RIGHT = new ImageView("/images/player/RIGHT.png");
        RIGHT_SMALL = new ImageView("/images/player/RIGHT_SMALL.png");

        CANNON = new ImageView(new Image("/images/player/player_cannon.png", 70, 70, true, true));

        lastUpdate = 0L;

        firing = new AnimationTimer() {

            @Override
            public void handle(long now) {

                long deltaTime = (now - lastUpdate) / 1000000;
                if (deltaTime >= (1000 / firingSpeed)) {
                    spawnProjectile(getCenterX() + 22, getCenterY() + 25, mouseX, mouseY);

                    lastUpdate = now;
                }
            }
        };
    }

    double getVelocityX() {
        return velocityX * speedCoefficient;
    }

    double getVelocityY() {
        return velocityY * speedCoefficient;
    }

    void startFiring() {
        firing.start();
    }

    void stopFiring() {
        firing.stop();
    }


    //    void setVelocity(double X, double Y) {
//        velocityX = X;
//        velocityY = Y;
//    }
    ImageView getUP() {
        UP.setX(getX() + 20);
        UP.setY(getY() + 45 + height);
        return UP;
    }

    ImageView getUP_SMALL() {
        UP_SMALL.setX(getX() + 22.5);
        UP_SMALL.setY(getY() + height + 50);
        return UP_SMALL;
    }

    ImageView getLEFT() {
        LEFT.setX(getX() + width + 45);
        LEFT.setY(getY() + 20);
        return LEFT;
    }

    ImageView getLEFT_SMALL() {
        LEFT_SMALL.setX(getX() + width + 50);
        LEFT_SMALL.setY(getY() + 22.5);
        return LEFT_SMALL;
    }

    ImageView getDOWN() {
        DOWN.setX(getX() + 20);
        DOWN.setY(getY() - 10);
        return DOWN;
    }

    ImageView getDOWN_SMALL() {
        DOWN_SMALL.setX(getX() + 22.5);
        DOWN_SMALL.setY(getY() - 5);
        return DOWN_SMALL;
    }

    ImageView getRIGHT() {
        RIGHT.setX(getX() - 10);
        RIGHT.setY(getY() + 20);
        return RIGHT;
    }

    ImageView getRIGHT_SMALL() {
        RIGHT_SMALL.setX(getX() - 5);
        RIGHT_SMALL.setY(getY() + 22.5);
        return RIGHT_SMALL;
    }

    ImageView getCANNON() {
        CANNON.setX(getX());
        CANNON.setY(getY());
        return CANNON;
    }

    void rotateCannon(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();

        double deltaX = getX() + width / 2 - event.getX();
        double deltaY = getY() + height / 2 - event.getY();
        double angle = -Math.toDegrees(Math.atan2(deltaX, deltaY));
        CANNON.setRotate(angle - 90);
    }

    void rotateCannon() {
        double deltaX = getX() + width / 2 - mouseX;
        double deltaY = getY() + height / 2 - mouseY;
        double angle = -Math.toDegrees(Math.atan2(deltaX, deltaY));
        CANNON.setRotate(angle - 90);
    }

    static double getDamage() {
        return damage;
    }

    private void setVelocityX(double x) {
        velocityX = x * speedCoefficient;
    }

    private void setVelocityY(double y) {
        velocityY = y * speedCoefficient;
    }

    void addVelocity(double x, double y) {
        velocityX += x * speedCoefficient;
        velocityY += y * speedCoefficient;
    }

    void update(double time, Pane gameBox) {

        if (Math.abs(velocityX) < 0.001) {
            velocityX = 0;
        }
        if (Math.abs(velocityY) < 0.001) {
            velocityY = 0;
        }

        double deltaX = velocityX * time;
        double deltaY = velocityY * time;

        double newX = getPositionX() + deltaX;
        double newY = getPositionY() + deltaY;
        if (!isIntersectWithEnemies(newX, newY, gameBox)) {
            if ((newX >= 0) && (newX + width + 50 <= getGameDimX())) {
                setPositionX(newX);
                CANNON.setX(newX - 5);
            } else if (newX <= 0) {
                setPositionX(0);
                CANNON.setX(-5);
                setVelocityX(0.01);
            } else {
                setPositionX(getGameDimX() - getWidth() - 50);
                CANNON.setX(getGameDimX() - getWidth() - 5);
                setVelocityX(-0.01);
            }

            if ((newY >= 0) && (newY + height + 50 <= getGameDimY())) {
                setPositionY(newY);
                CANNON.setY(newY + 10);
            } else if (newY < 0) {
                setPositionY(0);
                CANNON.setY(10);
                setVelocityY(0.01);
            } else {
                setPositionY(getGameDimY() - height - 50);
                CANNON.setY(getGameDimY() - height + 10);
                setVelocityY(-0.01);
            }
        }
    }

    public void setSpeedCoefficient(int speedCoefficient) {
        this.speedCoefficient = speedCoefficient;
    }

    private boolean isIntersectWithEnemies(double x, double y, Pane gameBox) {
        for (Node node : gameBox.getChildren()) {
            if (node instanceof Enemy) {
                Enemy enemy = (Enemy) node;
                if (enemy.intersects(x, y, playerImage.getWidth(), playerImage.getHeight())) {
                    setVelocityX(0);
                    setVelocityY(0);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Position: " + getPositionX() + "," + getPositionY() + " , Velocity: " + velocityX + "," + velocityY;
    }
}