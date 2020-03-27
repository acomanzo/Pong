package com.main;

import java.awt.*;

/**
 * Represents an enemy that moves up and down vertically.
 */
public class Enemy_V extends GameObject {

    private int HEIGHT;
    private int WIDTH;
    private Ball ball;
    private int health;
    private boolean dead;
    private boolean hit;
    private boolean hit_right;
    private boolean hit_left;
    private boolean hit_top;
    private boolean hit_bottom;

    public Enemy_V(int x, int y, int width, int height, Ball ball, Color color) {
        super(x, y, 0, 1, color);
        this.WIDTH = width;
        this.HEIGHT = height;
        makeIntegerCoordinates();
        this.ball = ball;
        health = 100;
        dead = false;
        hit = false;
        hit_right = false;
        hit_left = false;
        hit_top = false;
        hit_bottom = false;
    }

    public void makeCoordinates() {

    }

    public void makeIntegerCoordinates() {
        xInts = new int[] {x, x, x + 50, x + 50, x, x, x + 100, x + 100};
        yInts = new int[] {y, y + 20, y + 20, y + 60, y + 60, y + 80, y + 80, y};
    }

    public void tick() {
        if (hit == true) {
            health = health - 25;
            hit = false;
            System.out.println(health);
        }

        if (health <= 0) {
            dead = true;
        }

        yInts[0] += velY;
        yInts[1] += velY;
        yInts[2] += velY;
        yInts[3] += velY;
        yInts[4] += velY;
        yInts[5] += velY;
        yInts[6] += velY;
        yInts[7] += velY;

        listenForCollision();
    }

    public void render(Graphics g) {
        if (dead == false) {
            g.setColor(color);
            g.fillPolygon(xInts, yInts, 8);
        }

        if (hit_right) {
            g.setColor(Color.RED);
            g.drawLine(xInts[7], yInts[7], xInts[6], yInts[6]);
            g.drawLine(xInts[7], yInts[7] + 1, xInts[6], yInts[6] + 1);
            hit_right = false;
        }

        if (hit_left) {
            g.setColor(Color.RED);
            g.drawLine(xInts[0], yInts[0], xInts[1], yInts[1]);
            g.drawLine(xInts[0], yInts[0] + 1, xInts[1], yInts[1] + 1);
            hit_left = false;
        }

        if (hit_top) {
            g.setColor(Color.RED);
            g.drawLine(xInts[0], yInts[0], xInts[7], yInts[7]);
            g.drawLine(xInts[0], yInts[0] + 1, xInts[7], yInts[7] + 1);
            hit_top = false;
        }

        if (hit_bottom) {
            g.setColor(Color.RED);
            g.drawLine(xInts[1], yInts[1], xInts[7], yInts[7]);
            g.drawLine(xInts[1], yInts[1] + 1, xInts[7], yInts[7] + 1);
            hit_bottom = false;
        }
    }

    private void listenForCollision() {
        double[] ball_xInts = ball.getXCoordinates();
        double[] ball_yInts = ball.getYCoordinates();

        if (yInts[0] <= 90) {
            velY *= -1;
        }
        if (yInts[3] >= 700) {
            velY *= -1;
        }

        // if the ball hits the right side
        if (ball_xInts[0] < this.xInts[0] + WIDTH
                && ball_xInts[0] > this.xInts[0] + WIDTH - 10
                && ball_yInts[0] < this.yInts[0] + HEIGHT
                && ball_yInts[0] > this.yInts[0]) {
            hit = true;
            ball_xInts[0] = this.xInts[0] + WIDTH;
            ball_xInts[1] = this.xInts[1] + WIDTH;
            ball_xInts[2] = this.xInts[2] + ball.getWidth();
            ball_xInts[3] = this.xInts[3] + ball.getWidth();
            ball.setXCoordinates(ball_xInts);
            ball.setVelX(ball.getVelX() * -1);
            hit_right = true;
        }
        // if the ball hits the left side
        else if (ball_xInts[2] > this.xInts[0]
                && ball_xInts[2] < this.xInts[0] + 10
                && ball_yInts[2] < this.yInts[0] + HEIGHT
                && ball_yInts[2] > this.yInts[0]) {
            hit = true;
            ball_xInts[0] = this.xInts[0];
            ball_xInts[1] = this.xInts[1];
            ball_xInts[2] = this.xInts[1] - ball.getWidth();
            ball_xInts[3] = this.xInts[1] - ball.getWidth();
            ball.setXCoordinates(ball_xInts);
            ball.setVelX(ball.getVelX() * -1);
            hit_left = true;
        }
        // if the ball hits the bottom
        else if (ball_xInts[0] > this.xInts[0]
                && ball_xInts[0] < this.xInts[0] + WIDTH
                && ball_yInts[0] < this.yInts[0] + HEIGHT
                && ball_yInts[0] > this.yInts[0] + HEIGHT - 10) {
            hit = true;
            ball_yInts[0] = this.yInts[0] + HEIGHT;
            ball_yInts[1] = this.yInts[0] + HEIGHT + ball.getHeight();
            ball_yInts[2] = this.yInts[0] + HEIGHT + ball.getHeight();
            ball_yInts[3] = this.yInts[0] + HEIGHT;
            ball.setYCoordinates(ball_yInts);
            ball.setVelY(ball.getVelY() * -1);
            hit_bottom = true;
        }
        // if the ball hits the top
        else if (ball_xInts[0] > this.xInts[0]
                && ball_xInts[0] < this.xInts[0] + WIDTH
                && ball_yInts[2] > this.yInts[0]
                && ball_yInts[2] < this.yInts[0] + 10) {
            hit = true;
            ball_yInts[0] = this.yInts[0] + ball.getHeight();
            ball_yInts[1] = this.yInts[0];
            ball_yInts[2] = this.yInts[0];
            ball_yInts[3] = this.yInts[0] + ball.getHeight();
            ball.setYCoordinates(ball_yInts);
            ball.setVelY(ball.getVelY() * -1);
            hit_top = true;
        }
    }

    public void resurrect() {
        dead = false;
    }

    public boolean isDead() {
        return dead;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
