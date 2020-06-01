package com.main;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Represents an object in the game. Objects have an x and y indicating where they are initially drawn,
 * velocities in the x and y directions if they move.
 */
public abstract class SimpleGameObject implements GameObject {

    protected double HEIGHT;
    protected double WIDTH;

    protected boolean overlap;

    protected Point2D.Double midPoint;
    protected double velX, velY;

    protected Point2D.Double[] vertices;

    // represents the color to render this SimpleGameObject in
    protected Color color;

    protected String id;

    public SimpleGameObject(double x, double y, double velX, double velY, double height, double width, Color color, String id) {
        midPoint = new Point2D.Double(x, y);
        this.velX = velX;
        this.velY = velY;
        HEIGHT = height;
        WIDTH = width;
        this.color = color;
        this.id = id;
        makeCoordinates();

        overlap = false;
    }

    public void makeCoordinates() {
        Point2D.Double v1 = new Point2D.Double(midPoint.x - (WIDTH / 2), midPoint.y - (HEIGHT / 2));
        Point2D.Double v2 = new Point2D.Double(midPoint.x - (WIDTH / 2), midPoint.y + (HEIGHT / 2));
        Point2D.Double v3 = new Point2D.Double(midPoint.x + (WIDTH / 2), midPoint.y + (HEIGHT / 2));
        Point2D.Double v4 = new Point2D.Double(midPoint.x + (WIDTH / 2), midPoint.y - (HEIGHT / 2));

        vertices = new Point2D.Double[4];
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;
        vertices[3] = v4;

    }

    public void setX(double x) {
        midPoint.x = x;
        makeCoordinates();
    }

    public void setY(double y) {
        midPoint.y = y;
        makeCoordinates();
    }

    public double getX() {
        return midPoint.x;
    }

    public double getY() {
        return midPoint.y;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public Point2D[] getVertices() {
        return vertices;
    }

    public String getId() {
        return id;
    }
}
