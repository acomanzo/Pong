package com.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Represents the main menu when the game starts up. Also represents the pause menu.
 */
public class Menu extends MouseAdapter {

    private Game game;
    private Handler handler;

    private int mx;
    private int my;

    public Menu (Game game, Handler handler) {
        this.game = game;
        this.handler = handler;
    }

    public void tick() {
        mx = 0;
        my = 0;
    }

    public void render (Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, 1000, 1000 / 12 * 9);

        g.setColor(Color.GREEN);
        g.fillRect(100, 200, 800, 100);

        g.setColor(Color.ORANGE);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        g.drawString("Tony Pong Demo", 300, 50);
        g.drawString("Click here to play.", 200, 250);
    }

    public void mousePressed (MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        // play button
        if (mouseOver(mx, my, 100, 200, 800, 100)) {
            game.setState("PLAY");
        }
    }

    public void mouseReleased (MouseEvent e) {

    }

    private boolean mouseOver (int mx, int my, int x, int y, int width, int height) {
        if (mx > x && mx < x + width && my > y && my < y + height) {
            return true;
        }
         return false;
    }
}
