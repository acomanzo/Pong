package com.main;

import java.awt.*;

/**
 * Represents an object in the game. Objects have an x and y indicating where they are initially drawn,
 * velocities in the x and y directions if they move.
 */
public abstract class GameObject {

    protected int x, y;
    protected int velX, velY;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }


}
