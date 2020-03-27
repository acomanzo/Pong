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
    private int [] uprightXCoordinates;
    private int [] uprightYCoordinates;

    // don't use class Point because it forces you to use integer precision, which will cause the size of th e
    // paddle to shrink when you rotate it. Over time, data is lost.
    double midpointX;
    double midpointY;

    public TurnPaddle(int x, int y, int width, int height, Game game, Color color) {
        super(x, y, 0, 0, color);
        this.game = game;

        makeCoordinates();

        uprightXCoordinates = new int[]{x, x, x + WIDTH, x + WIDTH};
        uprightYCoordinates = new int[]{y, y + HEIGHT, y + HEIGHT, y};

        midpointX = (xCoordinates[0] + xCoordinates[2]) / 2;
        midpointY = (yCoordinates[0] + yCoordinates[2]) / 2;

    }

    public void makeCoordinates() {
        xCoordinates = new double[]{x, x, x + WIDTH, x + WIDTH};
        yCoordinates = new double[]{y, y + HEIGHT, y + HEIGHT, y};
    }

    @Override
    public void makeIntegerCoordinates() {
        xInts = new int[4];
        yInts = new int[4];
    }

    public void tick() {
        xCoordinates[0] += this.velX;
        xCoordinates[1] += this.velX;
        xCoordinates[2] += this.velX;
        xCoordinates[3] += this.velX;
        yCoordinates[0] += this.velY;
        yCoordinates[1] += this.velY;
        yCoordinates[2] += this.velY;
        yCoordinates[3] += this.velY;

        uprightXCoordinates[0] += this.velX;
        uprightXCoordinates[1] += this.velX;
        uprightXCoordinates[2] += this.velX;
        uprightXCoordinates[3] += this.velX;
        uprightYCoordinates[0] += this.velY;
        uprightYCoordinates[1] += this.velY;
        uprightYCoordinates[2] += this.velY;
        uprightYCoordinates[3] += this.velY;

        double radians = degrees * (Math.PI / 180);

        // rotate the top left vertex
        double x1 = xCoordinates[0] - midpointX;
        double y1 = yCoordinates[0] - midpointY;

        double x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        double y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        xCoordinates[0] = x2 + midpointX;
        yCoordinates[0] = y2 + midpointY;

        // rotate the bottom left vertex
        x1 = xCoordinates[1] - midpointX;
        y1 = yCoordinates[1] - midpointY;

        x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        xCoordinates[1] = x2 + midpointX;
        yCoordinates[1] = y2 + midpointY;

        // rotate the bottom right vertex
        x1 = xCoordinates[2] - midpointX;
        y1 = yCoordinates[2] - midpointY;

        x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        xCoordinates[2] = x2 + midpointX;
        yCoordinates[2] = y2 + midpointY;

        // rotate the top right vertex
        x1 = xCoordinates[3] - midpointX;
        y1 = yCoordinates[3] - midpointY;

        x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        xCoordinates[3] = x2 + midpointX;
        yCoordinates[3] = y2 + midpointY;

        // update the midpoint
        midpointX = (xCoordinates[0] + xCoordinates[2]) / 2;
        midpointY = (yCoordinates[0] + yCoordinates[2]) / 2;

        // fill integer arrays so that we can use fillPolygon() in render()
        for(int i = 0; i < xCoordinates.length; i++) {
            xInts[i] = (int) xCoordinates[i];
        }

        for(int i = 0; i < yCoordinates.length; i++) {
            yInts[i] = (int) yCoordinates[i];
        }

    }

    public void render(Graphics g) {
        g.setColor(color);

        g.fillPolygon(xInts, yInts, 4);

        if(game.toggleHitBoxes) {
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
        g.drawPolygon(uprightXCoordinates, uprightYCoordinates, 4);*/
    }

    public int getHEIGHT() {
        return this.HEIGHT;
    }

    public int getWIDTH() {
        return this.WIDTH;
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
        double c = Math.sqrt(Math.pow(xCoordinates[0] - uprightXCoordinates[0], 2) + Math.pow(yCoordinates[0] - uprightYCoordinates[0], 2));
        //System.out.println(c);

        // get side length a (position of top left vertex to midpoint of the paddle)
        double a = Math.sqrt(Math.pow(xCoordinates[0] - midpointX, 2) + Math.pow(yCoordinates[0] - midpointY, 2));
        //System.out.println(a);

        // get side length b (upright image of top left vertex to midpoint of the paddle)
        double b = Math.sqrt(Math.pow(uprightXCoordinates[0] - midpointX, 2) + Math.pow(uprightYCoordinates[0] - midpointY, 2));
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
