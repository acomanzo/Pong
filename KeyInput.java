package com.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles keyboard input.
 */
public class KeyInput extends KeyAdapter {

    Handler handler;
    Paddle player1;
    Paddle player2;
    Game game;
    TurnPaddle turnPaddle;

    int test = 0;

    public KeyInput(Handler handler, Paddle player1, Paddle player2, Game game, TurnPaddle turnPaddle) {
        this.handler = handler;
        this.player1 = player1;
        this.player2 = player2;
        this.game = game;
        this.turnPaddle = turnPaddle;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W) player1.setVelY(-8);
        if(key == KeyEvent.VK_S) player1.setVelY(8);

        /*
         * Consider using a KeyDown event to turn the paddle, because then that would streamline getting the
         * angle of rotation.
         */
        if(key == KeyEvent.VK_A) {
            turnPaddle.setDegrees(turnPaddle.getDegrees() - 5);
        }
        if(key == KeyEvent.VK_D) {
            turnPaddle.setDegrees(turnPaddle.getDegrees() + 5);
        }


        if(key == KeyEvent.VK_UP) turnPaddle.setVelY(-5);
        if(key == KeyEvent.VK_DOWN) turnPaddle.setVelY(5);
        //if(key == KeyEvent.VK_LEFT) player2.setVelX(-5);
        //if(key == KeyEvent.VK_RIGHT) player2.setVelX(5);

        if(key == KeyEvent.VK_ESCAPE) System.exit(1);

        if(key == KeyEvent.VK_SPACE) game.toggleHitboxes = !game.toggleHitboxes;

        if (key == KeyEvent.VK_LEFT) {
            turnPaddle.setVelX(-5);
        }
        if (key == KeyEvent.VK_RIGHT) {
            turnPaddle.setVelX(5);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W) player1.setVelY(0);
        if(key == KeyEvent.VK_S) player1.setVelY(0);
        if(key == KeyEvent.VK_A) {
            turnPaddle.setDegrees(0);
        }
        if(key == KeyEvent.VK_D) {
            turnPaddle.setDegrees(0);
        }

        if (key == KeyEvent.VK_LEFT) {
            turnPaddle.setVelX(0);
        }
        if (key == KeyEvent.VK_RIGHT) {
            turnPaddle.setVelX(0);
        }

        if(key == KeyEvent.VK_UP) turnPaddle.setVelY(0);
        if(key == KeyEvent.VK_DOWN) turnPaddle.setVelY(0);
        //if(key == KeyEvent.VK_LEFT) player2.setVelX(0);
        //if(key == KeyEvent.VK_RIGHT) player2.setVelX(0);
    }

    public void keyDown(KeyEvent e) {
        test++;
        System.out.println(test);
    }
}
