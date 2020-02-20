package com.main;

import java.awt.*;

/**
 * Represents a paddle that cannot rotate. Think of the traditionally pong paddle.
 */
public class Paddle extends GameObject {

    final int HEIGHT = 100;
    final int WIDTH = 20;
    Game game;

    int[] xPoints1;
    int[] yPoints1;

    public Paddle(int x, int y, Game game) {
        super(x, y);

        this.game = game;
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
        g.setColor(Color.BLACK);
        g.fillRect(x, y, WIDTH, HEIGHT);

        if(game.toggleHitboxes) {
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
        //return new Point((xPoints1[2] + xPoints1[3]) / 2, (yPoints1[2] + yPoints1[3]) / 2);
        return new Point((x + this.WIDTH + x + this.WIDTH) / 2, (y + this.HEIGHT + y) / 2);
    }

    /**
     * Returns the mid point of the top left and bottom left vertices of this paddle.
     * @return the mid point of the top left and bottom left vertices of this paddle.
     */
    public Point getLeftMidPoint() {
        return new Point((xPoints1[0] + xPoints1[1]) / 2, (yPoints1[0] + yPoints1[1]) / 2);
    }
}
