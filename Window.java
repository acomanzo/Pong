package com.main;

import javafx.scene.canvas.Canvas;
import javax.swing.*;
import java.awt.*;

/**
 * Represents the window that opens when the game starts.
 */
public class Window extends Canvas {

    public Window(int width, int height, Game game) {

        JFrame frame = new JFrame("pong");

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}
