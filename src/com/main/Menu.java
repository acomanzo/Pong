package com.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Represents the main menu when the game starts up. Also represents the pause menu.
 */
public class Menu extends MouseAdapter {

    private Game game;

    private int mx;
    private int my;

    public Menu (Game game) {
        this.game = game;
    }

    public void tick() {
        mx = 0;
        my = 0;
    }

    public void render (Graphics g) {
        Color myLightPurple = new Color(178, 152, 220);
        Color mySlate = new Color(184, 208, 235);
        Color myPurple = new Color (166, 99, 204);
        Color myDarkPurple = new Color (111, 76, 189);
        Color myPaleBlue = new Color(185, 250, 248);
        Color bang = new Color (228, 255, 81);

        g.setColor(myLightPurple);
        g.fillRect(0, 0, 1000, 1000 / 12 * 9);

        g.setColor(mySlate);
        g.fillRect(100, 200, 800, 100);
        g.fillRect(100, 400, 800, 100);

        g.setColor(myPurple);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        g.drawString("Click here to play.", 200, 260);
        g.drawString("Click here to play classic pong.", 200, 460);

        g.setColor(bang);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 36));
        g.drawString("Crazy Pong!", 300, 80);
    }

    public void mousePressed (MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        // crazy play button
        if (mouseOver(mx, my, 100, 200, 800, 100)) {
            game.setState("CRAZY_MODE");
        }

        // classic pong
        if (mouseOver(mx, my, 100, 400, 800, 100)) {
            game.setState("CLASSIC_MODE");
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
