package com.main;

import java.awt.*;

public class CircleBall extends GameObject {

    private final int HEIGHT = 20, WIDTH = 20;

    private int velX = 4;
    private int velY = -2;

    private Paddle player1;
    private TurnPaddle turnPaddle;

    private int speedBoost = 0;

    private CPUPaddle cpuPaddle;

    private Timer timer;

    private Handler handler;

    private boolean scored = true;

    private long startStamp = 0;

    private Game game;

    public CircleBall (int x, int y, Paddle player1, TurnPaddle turnPaddle, Timer timer, Handler handler, Game game, CPUPaddle cpuPaddle) {
        super(x, y);
        this.player1 = player1;
        this.turnPaddle = turnPaddle;
        this.timer = timer;
        this.handler = handler;
        this.game = game;
        this.cpuPaddle = cpuPaddle;
    }

    public void tick() {

        x += velX;
        y += velY;

        if(speedBoost > 1) {
            if(velX < 0) {
                x -= speedBoost;
            }

            else if(velX > 0) {
                x += speedBoost;
            }

            if(velY < 0) {
                y -= speedBoost;
            }
            else if(velY > 0) {
                y += speedBoost;

            }
        }

        long time = timer.getElapsedSeconds();

        if(scored == true) {
            startStamp = timer.getElapsedSeconds();
            scored = false;
            game.countdownStarted = false;
            timer.countdown = 4;
        }
        if(time - startStamp < 4) {
            x -= velX;

            y -= velY;

        }

        collision();

        if(speedBoost < 0)
            speedBoost = 0;

    }

    public void render(Graphics g) {
        g.setColor(Color.GREEN);

        if(game.toggleHitboxes) {
            g.setColor(Color.GREEN);
            g.drawOval(x, y, WIDTH, HEIGHT);
        }

        g.fillOval(x, y, WIDTH, HEIGHT);
    }

    private void collision() {


    }
}
