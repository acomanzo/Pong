package com.main;

import java.awt.*;
import java.util.TimerTask;

/**
 * Represents a timer. Used for countdowns at the beginning of the game, after a goal is scored, and
 * once the player returns from the pause menu.
 */
public class Timer {

    private long startTime;
    private long elapsedSeconds = 0;
    private long menuStartStamp;
    private long elapsedSecondsInMenu = 0;
    private long totalSecondsInMenu = 0;

    private boolean menuStartStampUpdated = false;
    private boolean totalSecondsInMenuUpdated = false;

    private Game game;
    private Paddle player1;
    private Paddle player2;
    private Ball ball;

    static long countdown = 4;

    public Timer(Game game, Paddle player1, Paddle player2) {
        this.game = game;
        this.player1 = player1;
        this.player2 = player2;
        this.ball = ball;
        startTime = System.currentTimeMillis();
        menuStartStamp = System.currentTimeMillis();
    }

    public void tick (boolean countdownStarted) {
        long elapsedTime = 0;
        elapsedTime = System.currentTimeMillis() - startTime;
        elapsedSeconds = elapsedTime / 1000;

        if (game.getState().equals("MAIN MENU")) {
            totalSecondsInMenuUpdated = false;

            if (menuStartStampUpdated == false) {
                menuStartStamp = System.currentTimeMillis();
                menuStartStampUpdated = true;
            }

            long elapsedTimeInMenu = System.currentTimeMillis() - menuStartStamp;
            elapsedSecondsInMenu = elapsedTimeInMenu / 1000;
        }

        if (game.getState().equals("PLAY")) {
            menuStartStampUpdated = false;
            if (totalSecondsInMenuUpdated == false) {
                totalSecondsInMenu += elapsedSecondsInMenu;
                totalSecondsInMenuUpdated = true;
            }
        }

        if(countdownStarted == false && game.getState().equals("PLAY")) {
            countdown();
        }
        //countdown(countdownStarted);
    }

    public void countdown() {
        java.util.Timer t = new java.util.Timer();
        /*if(countdownStarted == false) {
            //System.out.println("hi");
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    countdown--;
                    // this if block MUST be inside the run method!!! otherwise it won't cancel!!!
                    if(countdown <= 0) {
                        t.cancel();
                        t.purge();
                    }
                }
            }, 0, 1000);
        }*/
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                countdown--;
                // this if block MUST be inside the run method!!! otherwise it won't cancel!!!
                if(countdown <= 0) {
                    t.cancel();
                    t.purge();
                }
            }
        }, 0, 1000);
    }

    public void render(Graphics g) {
        /*if(elapsedSeconds == 11) {
            g.drawString(elapsedSeconds - 10 + "", 50, 50);
            g.setColor(Color.BLACK);
        }
        else {
            g.drawString(elapsedSeconds + "", 50, 50);
            g.setColor(Color.BLACK);
        }*/

        g.setColor(Color.WHITE);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        g.drawString("Time: " + (elapsedSeconds - totalSecondsInMenu), 15, 25);

        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

        if(countdown == 0) {
            g.drawString("GO!", 500, 50);
        }
        else
            g.drawString(countdown + "", 500, 50);

        //g.drawString(countdown + "", 500, 50);
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public long getStartTime() {
        return startTime;
    }
}
