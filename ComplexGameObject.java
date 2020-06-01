package com.main;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Represents a concave shape in the game.
 */
public abstract class ComplexGameObject extends SimpleGameObject {

    /*protected double HEIGHT;
    protected double WIDTH;

    protected boolean overlap;

    protected Point2D.Double midPoint;
    protected double velX, velY;*/

    // represents a set of vertices we use to pretend this is a convex shape
    protected Point2D.Double[] convexVertices;

    // the actual vertices that will render
    protected Point2D.Double[] concaveVertices;

    // represents the color to render this SimpleGameObject in
    /*protected Color color;

    protected String id;*/

    public ComplexGameObject(double x, double y, double velX, double velY, double height, double width, Color color, String id) {
        super(x, y, velX, velY, height, width, color, id);
        midPoint = new Point2D.Double(x, y);
        /*this.velX = velX;
        this.velY = velY;
        HEIGHT = height;
        WIDTH = width;
        this.color = color;
        this.id = id;*/

        makeConvexVertices();
        makeConcaveVertices();
    }

    public abstract void makeConvexVertices();

    public abstract void makeConcaveVertices();

    public double getX() {
        return midPoint.x;
    }

    public double getY() {
        return midPoint.y;
    }

    public void setX(double x) {
        midPoint.x = x;
        makeConvexVertices();
        makeConcaveVertices();
    }

    public void setY(double y) {
        midPoint.y = y;
        makeConvexVertices();
        makeConcaveVertices();
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public Point2D[] getConcaveVertices() { return concaveVertices; }

    public Point2D[] getVertices() {
        return convexVertices;
    }

    public String getId() {
        return id;
    }
}
