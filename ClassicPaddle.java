package com.main;

import java.awt.*;

/**
 * Represents a traditional Pong paddle.
 */
public class ClassicPaddle extends GameObject {

    private int HEIGHT;
    private int WIDTH;
    private Game game;

    public ClassicPaddle(int x, int y, int width, int height, Game game, Color color) {
        super(x, y, 0, 0, color);

        this.WIDTH = width;
        this.HEIGHT = height;
        makeCoordinates();
        this.game = game;
    }

    public void makeCoordinates() {
        xCoordinates = new double[]{x, x, x + WIDTH, x + WIDTH};
        yCoordinates = new double[]{y, y + HEIGHT, y + HEIGHT, y};
    }

    public void makeIntegerCoordinates() {
        xInts = new int[]{x, x, x + WIDTH, x + WIDTH};
        yInts = new int[]{y, y + HEIGHT, y + HEIGHT, y};
    }

    public void tick() {
        x += velX;
        y += velY;

        if(y <= 0)
            y = 0;
        if(y > game.HEIGHT - HEIGHT - 23)
            y = game.HEIGHT - HEIGHT - 23;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x += x;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, WIDTH, HEIGHT);

        if(game.toggleHitBoxes) {
            g.setColor(Color.GREEN);

            g.drawRect(x, y, WIDTH, 40);
            g.drawRect(x, y + 40, WIDTH, 20);
            g.drawRect(x, y + 60, WIDTH, 40);

        }
        else {
            g.setColor(Color.CYAN);
            g.drawLine(x, y, x, y + HEIGHT);
            g.drawLine(x + 1, y, x + 1, y + HEIGHT);
            g.drawLine(x + WIDTH, y, x + WIDTH, y + HEIGHT);
            g.drawLine(x + WIDTH - 1, y, x + WIDTH - 1, y + HEIGHT);
        }

    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() { return HEIGHT; }

    /**
     * Returns the mid point of the top right and bottom right vertices of this paddle.
     * @return the mid point of the top right and bottom right vertices of this paddle.
     */
    public Point getRightMidPoint() {
        return new Point((x + this.WIDTH + x + this.WIDTH) / 2, (y + this.HEIGHT + y) / 2);
    }

    /**
     * Returns the mid point of the top left and bottom left vertices of this paddle.
     * @return the mid point of the top left and bottom left vertices of this paddle.
     */
    public Point getLeftMidPoint() {
        return new Point((xInts[0] + xInts[1]) / 2, (yInts[0] + yInts[1]) / 2);
    }
}
