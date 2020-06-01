package com.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles keyboard input.
 */
public class KeyInput extends KeyAdapter {

    private ClassicPaddle player1;
    private ClassicPaddle classicModePlayer;
    private Game game;
    private TurnPaddle turnPaddle;

    public KeyInput(ClassicPaddle player1, ClassicPaddle classicModePlayer, Game game, TurnPaddle turnPaddle) {
        this.player1 = player1;
        this.classicModePlayer = classicModePlayer;
        this.game = game;
        this.turnPaddle = turnPaddle;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (game.getState().equals("CRAZY_MODE")) {
            if(key == KeyEvent.VK_W) player1.setVelY(-8);
            if(key == KeyEvent.VK_S) player1.setVelY(8);

            if(key == KeyEvent.VK_A) {
                turnPaddle.decrementDegrees();
            }
            if(key == KeyEvent.VK_D) {
                turnPaddle.incrementDegrees();
            }

            if(key == KeyEvent.VK_UP) turnPaddle.setVelY(-5);
            if(key == KeyEvent.VK_DOWN) turnPaddle.setVelY(5);

            if (key == KeyEvent.VK_LEFT) {
                turnPaddle.setVelX(-5);
            }
            if (key == KeyEvent.VK_RIGHT) {
                turnPaddle.setVelX(5);
            }
        }
        else {
            if (key == KeyEvent.VK_UP) classicModePlayer.setVelY(-5);
            if (key == KeyEvent.VK_DOWN) classicModePlayer.setVelY(5);
        }

        if(key == KeyEvent.VK_ESCAPE) System.exit(1);
        if(key == KeyEvent.VK_SPACE) game.toggleHitBoxes = !game.toggleHitBoxes;

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (game.getState().equals("CRAZY_MODE")) {
            if(key == KeyEvent.VK_W) player1.setVelY(0);
            if(key == KeyEvent.VK_S) player1.setVelY(0);

            if (key == KeyEvent.VK_LEFT) {
                turnPaddle.setVelX(0);
            }
            if (key == KeyEvent.VK_RIGHT) {
                turnPaddle.setVelX(0);
            }

            if(key == KeyEvent.VK_UP) turnPaddle.setVelY(0);
            if(key == KeyEvent.VK_DOWN) turnPaddle.setVelY(0);
        }
        else {
            if (key == KeyEvent.VK_UP) classicModePlayer.setVelY(0);
            if(key == KeyEvent.VK_DOWN) classicModePlayer.setVelY(0);
        }
    }
}
