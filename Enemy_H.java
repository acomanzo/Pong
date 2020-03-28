package com.main;

import java.awt.*;

/**
 * Represents an enemy that moves side to side horizontally
 */
public class Enemy_H extends GameObject {

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

    public Enemy_H(int x, int y, int width, int height, Ball ball, Color color) {
        super(x, y, 2, 0, color);
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
        xInts = new int[] {x, x, x + 20, x + 20, x + 60, x + 60, x + 80, x + 80};
        yInts = new int[] {y, y + 100, y + 100, y + 50, y + 50, y + 100, y + 100, y};
    }

    public void setXInts(int x0, int x1, int x2, int x3, int x4, int x5, int x6, int x7) {
        xInts[0] = x0;
        xInts[1] = x1;
        xInts[2] = x2;
        xInts[3] = x3;
        xInts[4] = x4;
        xInts[5] = x5;
        xInts[6] = x6;
        xInts[7] = x7;
    }

    public void setYInts(int y0, int y1, int y2, int y3, int y4, int y5, int y6, int y7) {
        yInts[0] = y0;
        yInts[1] = y1;
        yInts[2] = y2;
        yInts[3] = y3;
        yInts[4] = y4;
        yInts[5] = y5;
        yInts[6] = y6;
        yInts[7] = y7;
    }

    public void tick() {
        if (hit == true) {
            health = health - 25;
            hit = false;
        }

        if (health <= 0) {
            dead = true;
        }

        xInts[0] += velX;
        xInts[1] += velX;
        xInts[2] += velX;
        xInts[3] += velX;
        xInts[4] += velX;
        xInts[5] += velX;
        xInts[6] += velX;
        xInts[7] += velX;

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

        // if this is too far right
        if (xInts[7] >= 700) {
            velX *= -1;
        }
        // if this is too far left
        if (xInts[0] <= 90) {
            velX *= -1;
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
