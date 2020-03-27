package com.main;

import java.awt.*;
import java.util.TimerTask;

/**
 * Represents a timer. Used for countdowns at the beginning of the game, after a goal is scored, and
 * once the player returns from the pause menu; and keeps track of the total time of the mode.
 */
public class Timer {

    private long startTime;
    private long elapsedSeconds = 0;

    private Game game;

    private Color color;

    // represents the mode during which this timer should be active
    private String mode;

    static long countdown = 4;

    public Timer(Game game, String mode, Color color) {
        this.game = game;
        startTime = System.currentTimeMillis();
        this.mode = mode;
        this.color = color;
    }

    public void tick (boolean countdownStarted) {

        if(!countdownStarted && game.getState().equals(mode)) {
            countdown();
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        elapsedSeconds = (elapsedTime / 1000);

        if (!game.getState().equals(mode)) {
            pause();
        }
    }

    /**
     * Pauses the timer.
     */
    public void pause() {
        // TODO
    }

    /**
     * Decrements the countdown variable by one until it's less than or equal to zero.
     * Used when the game starts, after the player returns from the pause menu, and when a goal is scored.
     */
    public void countdown() {
        java.util.Timer t = new java.util.Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                countdown--;
                if(countdown <= 0) {
                    game.setPauseCrazyModeBall(false);
                    t.cancel();
                    t.purge();
                }
            }
        }, 0, 1000);
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        g.drawString("Time: " + (elapsedSeconds), 15, 25);

        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

        if (countdown == 0) {
            g.drawString("GO!", 500, 50);
        }
        else if (countdown > 0) {
            g.drawString(countdown + "", 500, 50);
        }
        else {
            g.drawString(countdown + "Error in timer", 500, 50);
        }

    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public long getStartTime() {
        return startTime;
    }
}
