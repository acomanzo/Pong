package com.main;

import java.awt.*;

/**
 * Represents an object in the game. Objects have an x and y indicating where they are initially drawn,
 * velocities in the x and y directions if they move.
 */
public abstract class GameObject {

    protected int x, y;
    protected int velX, velY;

    // arrays we use to not lose data precision. [0] = top left, [1] = bottom left, [2] = bottom right, [3] = top right
    protected double[] xCoordinates;
    protected double[] yCoordinates;

    // arrays we use with the native fillPolygon() class because it won't accept double.
    protected int[] xInts = new int[4];
    protected int[] yInts = new int[4];

    // represents the color to render this GameObject in
    protected Color color;

    public GameObject(int x, int y, int velX, int velY, Color color) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.color = color;
    }

    public abstract void makeCoordinates();
    public abstract void makeIntegerCoordinates();

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

    public double[] getXCoordinates() {
        return xCoordinates;
    }

    public double[] getYCoordinates() {
        return yCoordinates;
    }

    public void setXCoordinates(double x0, double x1, double x2, double x3) {
        this.xCoordinates[0] = x0;
        this.xCoordinates[1] = x1;
        this.xCoordinates[2] = x2;
        this.xCoordinates[3] = x3;
    }

    public void setYCoordinates(double y0, double y1, double y2, double y3) {
        this.yCoordinates[0] = y0;
        this.yCoordinates[1] = y1;
        this.yCoordinates[2] = y2;
        this.yCoordinates[3] = y3;
    }

    public int[] getXInts() {
        return xInts;
    }

    public int[] getYInts() {
        return yInts;
    }

}
