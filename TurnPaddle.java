package com.main;

import java.awt.*;

/**
 * Represents a paddle that can move up, down, left and right and can rotate.
 */
public class TurnPaddle extends GameObject {

    final int HEIGHT = 100;
    final int WIDTH = 20;
    private Game game;

    // the angle to rotate the paddle this tick in degrees
    private int degrees = 0;
    // the total angle the paddle is rotated in degrees
    private int degreesRotated = 0;

    // sets of the original points that we use to construct triangles to find the angle of rotation
    // since there's no KeyDown event in java that lets us keep track of the angle of rotation.
    // We add displacement up/down left/right so that we know the location of the upright position relative
    // to this paddle's current position and orientation.
    private int [] uprightXPoints;
    private int [] uprightYPoints;

    // arrays we use to not lose data precision. [0] = top left, [1] = bottom left, [2] = bottom right, [3] = top right
    private double[] xPoints;
    private double[] yPoints;

    // arrays we use with the native fillPolygon() class because it won't accept double.
    int[] xInts = new int[4];
    int[] yInts = new int[4];

    // don't use class Point because it forces you to use integer precision, which will cause the size of th e
    // paddle to shrink when you rotate it. Over time, data is lost.
    double midpointX;
    double midpointY;

    public TurnPaddle(int x, int y, Game game) {
        super(x, y);
        this.game = game;

        xPoints = new double[]{x, x, x + WIDTH, x + WIDTH};
        yPoints = new double[]{y, y + HEIGHT, y + HEIGHT, y};

        uprightXPoints = new int[]{x, x, x + WIDTH, x + WIDTH};
        uprightYPoints = new int[]{y, y + HEIGHT, y + HEIGHT, y};

        midpointX = (xPoints[0] + xPoints[2]) / 2;
        midpointY = (yPoints[0] + yPoints[2]) / 2;

    }

    public void tick() {
        xPoints[0] += this.velX;
        xPoints[1] += this.velX;
        xPoints[2] += this.velX;
        xPoints[3] += this.velX;
        yPoints[0] += this.velY;
        yPoints[1] += this.velY;
        yPoints[2] += this.velY;
        yPoints[3] += this.velY;

        uprightXPoints[0] += this.velX;
        uprightXPoints[1] += this.velX;
        uprightXPoints[2] += this.velX;
        uprightXPoints[3] += this.velX;
        uprightYPoints[0] += this.velY;
        uprightYPoints[1] += this.velY;
        uprightYPoints[2] += this.velY;
        uprightYPoints[3] += this.velY;

        double radians = degrees * (Math.PI / 180);

        // rotate the top left vertex
        double x1 = xPoints[0] - midpointX;
        double y1 = yPoints[0] - midpointY;

        double x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        double y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        xPoints[0] = x2 + midpointX;
        yPoints[0] = y2 + midpointY;

        // rotate the bottom left vertex
        x1 = xPoints[1] - midpointX;
        y1 = yPoints[1] - midpointY;

        x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        xPoints[1] = x2 + midpointX;
        yPoints[1] = y2 + midpointY;

        // rotate the bottom right vertex
        x1 = xPoints[2] - midpointX;
        y1 = yPoints[2] - midpointY;

        x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        xPoints[2] = x2 + midpointX;
        yPoints[2] = y2 + midpointY;

        // rotate the top right vertex
        x1 = xPoints[3] - midpointX;
        y1 = yPoints[3] - midpointY;

        x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        xPoints[3] = x2 + midpointX;
        yPoints[3] = y2 + midpointY;

        // update the midpoint
        midpointX = (xPoints[0] + xPoints[2]) / 2;
        midpointY = (yPoints[0] + yPoints[2]) / 2;

        // fill integer arrays so that we can use fillPolygon() in render()
        for(int i = 0; i < xPoints.length; i++) {
            xInts[i] = (int) xPoints[i];
        }

        for(int i = 0; i < yPoints.length; i++) {
            yInts[i] = (int) yPoints[i];
        }

    }

    public void render(Graphics g) {
        g.setColor(Color.PINK);

        g.fillPolygon(xInts, yInts, 4);

        if(game.toggleHitboxes) {
            g.setColor(Color.GREEN);

            g.drawPolygon(xInts, yInts, 4);
        }
        else {
            g.setColor(Color.CYAN);

            g.drawLine(xInts[0], yInts[0], xInts[1], yInts[1]);
            g.drawLine(xInts[0] + 1, yInts[0], xInts[1] + 1, yInts[1]); // just to make the line thicker

            g.drawLine(xInts[2], yInts[2], xInts[3], yInts[3]);
            g.drawLine(xInts[2] - 1, yInts[2], xInts[3] - 1, yInts[3]);
        }

        // for debugging
        /*g.setColor(Color.ORANGE);
        g.drawPolygon(uprightXPoints, uprightYPoints, 4);*/
    }

    public int getHEIGHT() {
        return this.HEIGHT;
    }

    public int getWIDTH() {
        return this.WIDTH;
    }

    public double[] getxPoints() {
        return this.xPoints;
    }

    public double[] getyPoints() {
        return this.yPoints;
    }

    public void setxPoints(TurnPaddle turnPaddle, double x0, double x1, double x2, double x3) {
        this.xPoints[0] = x0;
        this.xPoints[1] = x1;
        this.xPoints[2] = x2;
        this.xPoints[3] = x3;
    }

    public void setyPoints(TurnPaddle turnPaddle, double y0, double y1, double y2, double y3) {
        this.yPoints[0] = y0;
        this.yPoints[1] = y1;
        this.yPoints[2] = y2;
        this.yPoints[3] = y3;
    }

    public double getMidpointX() {
        return this.midpointX;
    }

    public double getMidpointY() {
        return this.midpointY;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public int getDegrees() {
        return degrees;
    }

    /**
     * Uses the law of cosines, this paddle's current orientation, and the image of this paddle's upright position
     * relative to this paddle's current position (x,y) to find the angle of rotation in radians.
     * @return the angle of rotation in radians
     */
    public double getDegreesRotated() {
        // law of cosines: c^2 = a^2 + b^2 - 2abcos(theta)

        // get side length c (position of top left vertex to top left vertex of the upright image)
        double c = Math.sqrt(Math.pow(xPoints[0] - uprightXPoints[0], 2) + Math.pow(yPoints[0] - uprightYPoints[0], 2));
        //System.out.println(c);

        // get side length a (position of top left vertex to midpoint of the paddle)
        double a = Math.sqrt(Math.pow(xPoints[0] - midpointX, 2) + Math.pow(yPoints[0] - midpointY, 2));
        //System.out.println(a);

        // get side length b (upright image of top left vertex to midpoint of the paddle)
        double b = Math.sqrt(Math.pow(uprightXPoints[0] - midpointX, 2) + Math.pow(uprightYPoints[0] - midpointY, 2));
        //System.out.println(b);

        // solve for angle = arccos[(c^2 - a^2 - b^2) / -2ab]
        // returns in radians
        double x = 0;
        x = Math.pow(c, 2) - Math.pow(a, 2) - Math.pow(b, 2);
        x = x / (-2 * a * b);
        x = Math.acos(x);

        return Math.acos(((Math.pow(c, 2) - Math.pow(a, 2) - Math.pow(b, 2)) / (-2 * a * b)));
    }

}
