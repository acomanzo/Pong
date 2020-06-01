package com.main;

import java.awt.*;

/**
 * Represents a trail on a ball.
 */
public class Trail implements GameObject {

    private double x;
    private double y;
    private float alpha = 1;
    private Handler handler;
    private Color color;
    private int width, height;
    private float life;
    private String id;

    // life = 0.01 - 0.1. smaller the life, the longer the particle

    public Trail(double x, double y, Color color, int width, int height, float life, Handler handler) {
        this.x = x;
        this. y = y;
        this.color = color;
        this.width = width;
        this.height = height;
        this.life = life;
        this.handler = handler;
        id = String.valueOf(String.valueOf(System.currentTimeMillis()));
    }

    public void tick() {
        if (alpha > life) {
            alpha -= life - 0.001f;
        }
        //else
        //    handler.removeObject(id);
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(makeTransparent(alpha));
        g.setColor(color);
        g.fillRect((int) x, (int) y, width, height);

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

    public String getId() {
        return id;
    }
}
