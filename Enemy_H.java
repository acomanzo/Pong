package com.main;

import java.awt.*;

/**
 * Represents an enemy that moves side to side horizontally
 */
public class Enemy_H extends GameObject {

    private int[] x_points;
    private int[] y_points;
    private final int HEIGHT = 100;
    private final int WIDTH = 80;
    private Ball ball;
    private int health;
    private boolean dead;
    private boolean hit;
    private boolean hit_right;
    private boolean hit_left;
    private boolean hit_top;
    private boolean hit_bottom;
    private int velX = 2;

    public Enemy_H(int x, int y, Ball ball) {
        super(x, y);
        x_points = new int[] {x, x, x + 20, x + 20, x + 60, x + 60, x + 80, x + 80};
        y_points = new int[] {y, y + 100, y + 100, y + 50, y + 50, y + 100, y + 100, y};
        this.ball = ball;
        health = 100;
        dead = false;
        hit = false;
        hit_right = false;
        hit_left = false;
        hit_top = false;
        hit_bottom = false;
    }

    public void tick() {
        if (hit == true) {
            health = health - 25;
            hit = false;
        }

        if (health <= 0) {
            dead = true;
        }

        x_points[0] += velX;
        x_points[1] += velX;
        x_points[2] += velX;
        x_points[3] += velX;
        x_points[4] += velX;
        x_points[5] += velX;
        x_points[6] += velX;
        x_points[7] += velX;

        listenForCollision();
    }

    public void render(Graphics g) {
        if (dead == false) {
            g.setColor(Color.BLACK);
            g.fillPolygon(x_points, y_points, 8);
        }

        if (hit_right) {
            g.setColor(Color.RED);
            g.drawLine(x_points[7], y_points[7], x_points[6], y_points[6]);
            g.drawLine(x_points[7], y_points[7] + 1, x_points[6], y_points[6] + 1);
            hit_right = false;
        }

        if (hit_left) {
            g.setColor(Color.RED);
            g.drawLine(x_points[0], y_points[0], x_points[1], y_points[1]);
            g.drawLine(x_points[0], y_points[0] + 1, x_points[1], y_points[1] + 1);
            hit_left = false;
        }

        if (hit_top) {
            g.setColor(Color.RED);
            g.drawLine(x_points[0], y_points[0], x_points[7], y_points[7]);
            g.drawLine(x_points[0], y_points[0] + 1, x_points[7], y_points[7] + 1);
            hit_top = false;
        }

        if (hit_bottom) {
            g.setColor(Color.RED);
            g.drawLine(x_points[1], y_points[1], x_points[7], y_points[7]);
            g.drawLine(x_points[1], y_points[1] + 1, x_points[7], y_points[7] + 1);
            hit_bottom = false;
        }

    }

    private void listenForCollision() {
        double[] ball_x_points = ball.getxPoints();
        double[] ball_y_points = ball.getyPoints();

        // if this is too far right
        if (x_points[7] >= 700) {
            velX *= -1;
        }
        // if this is too far left
        if (x_points[0] <= 90) {
            velX *= -1;
        }

        // if the ball hits the right side
        if (ball_x_points[0] < this.x_points[0] + WIDTH
                && ball_x_points[0] > this.x_points[0] + WIDTH - 10
                && ball_y_points[0] < this.y_points[0] + HEIGHT
                && ball_y_points[0] > this.y_points[0]) {
            hit = true;
            ball_x_points[0] = this.x_points[0] + WIDTH;
            ball_x_points[1] = this.x_points[1] + WIDTH;
            ball_x_points[2] = this.x_points[2] + ball.getWidth();
            ball_x_points[3] = this.x_points[3] + ball.getWidth();
            ball.set_x_points(ball_x_points);
            ball.setVelX(ball.getVelX() * -1);
            hit_right = true;
        }
        // if the ball hits the left side
        else if (ball_x_points[2] > this.x_points[0]
                && ball_x_points[2] < this.x_points[0] + 10
                && ball_y_points[2] < this.y_points[0] + HEIGHT
                && ball_y_points[2] > this.y_points[0]) {
            hit = true;
            ball_x_points[0] = this.x_points[0];
            ball_x_points[1] = this.x_points[1];
            ball_x_points[2] = this.x_points[1] - ball.getWidth();
            ball_x_points[3] = this.x_points[1] - ball.getWidth();
            ball.set_x_points(ball_x_points);
            ball.setVelX(ball.getVelX() * -1);
            hit_left = true;
        }
        // if the ball hits the bottom
        else if (ball_x_points[0] > this.x_points[0]
                && ball_x_points[0] < this.x_points[0] + WIDTH
                && ball_y_points[0] < this.y_points[0] + HEIGHT
                && ball_y_points[0] > this.y_points[0] + HEIGHT - 10) {
            hit = true;
            ball_y_points[0] = this.y_points[0] + HEIGHT;
            ball_y_points[1] = this.y_points[0] + HEIGHT + ball.getHeight();
            ball_y_points[2] = this.y_points[0] + HEIGHT + ball.getHeight();
            ball_y_points[3] = this.y_points[0] + HEIGHT;
            ball.set_y_points(ball_y_points);
            ball.setVelY(ball.getVelY() * -1);
            hit_bottom = true;
        }
        // if the ball hits the top
        else if (ball_x_points[0] > this.x_points[0]
                && ball_x_points[0] < this.x_points[0] + WIDTH
                && ball_y_points[2] > this.y_points[0]
                && ball_y_points[2] < this.y_points[0] + 10) {
            hit = true;
            ball_y_points[0] = this.y_points[0] + ball.getHeight();
            ball_y_points[1] = this.y_points[0];
            ball_y_points[2] = this.y_points[0];
            ball_y_points[3] = this.y_points[0] + ball.getHeight();
            ball.set_y_points(ball_y_points);
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
