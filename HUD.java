package com.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Represents a heads-up-display. Displays score, time and a pause button.
 */
public class HUD extends MouseAdapter {

    private final int HEIGHT = 150;
    private final int WIDTH = 200;

    private int x;
    private int y;

    private int mx;
    private int my;

    private int player1Score;
    private int cpuScore;

    private Game game;
    private Timer timer;
    private Ball ball;

    private Color color;

    public HUD(int x, int y, Game game, Timer timer, Ball ball, Color color) {
        this.x = x;
        this.y = y;
        this.game = game;
        this.timer = timer;
        this.ball = ball;
        this.color = color;
    }

    public void tick(int player1Score, int cpuScore) {
        this.player1Score = player1Score;
        this.cpuScore = cpuScore;

        mx = 0;
        my = 0;
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.drawRect(x, y, WIDTH, HEIGHT);

        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        g.drawString("Player 1 Score: " + player1Score, x + 10, y + 45);
        g.drawString("CPU Score: " + cpuScore, x + 10, y + 70);

        g.drawRect(x + 10, y + 95, 50, 30);
        g.drawString("Pause", x + 15, y + 115);

    }

    public void mousePressed (MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        // pause button
        if (mouseOver(mx, my, x + 10, y + 95, 50, 30)) {
            game.setState("MAIN MENU");

            // mimic what happens when you score... pause the ball and do a countdown
            ball.setScored(true);
        }
    }

    private boolean mouseOver (int mx, int my, int x, int y, int width, int height) {
        if (mx > x && mx < x + width && my > y && my < y + height) {
            return true;
        }
        return false;
    }

}
