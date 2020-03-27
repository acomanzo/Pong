package com.main;

import java.awt.*;

/**
 * Represents a trail on a ball.
 */
public class Trail extends GameObject {

    private float alpha = 1;
    private Handler handler;
    private Color color;
    private int width, height;
    private float life;

    // life = 0.01 - 0.1. smaller the life, the longer the particle

    public Trail(int x, int y, Color color, int width, int height, float life, Handler handler) {
        super(x, y, 0, 0, color);
        this.color = color;
        this.width = width;
        this.height = height;
        this.life = life;
        this.handler = handler;
    }

    @Override
    public void makeCoordinates() {

    }

    @Override
    public void makeIntegerCoordinates() {

    }

    public void tick() {
        if(alpha > life) {
            alpha -= life - 0.001f;
        }
        else
            handler.removeObject(this);
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(makeTransparent(alpha));
        g.setColor(color);
        g.fillRect(x, y, width, height);

        g2d.setComposite(makeTransparent(1));
    }

    /**
     * This method renders transparencies in objects
     * @param alhpa
     * @return
     */
    private AlphaComposite makeTransparent(float alhpa) {
        int type = AlphaComposite.SRC_OVER;
        return AlphaComposite.getInstance(type, alpha);
    }

    public Rectangle getBounds() {
        return null;
    }
}
