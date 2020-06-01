package com.main;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Represents a paddle that can move up, down, left and right and can rotate.
 */
public class TurnPaddle extends SimpleGameObject {

    private Game game;

    // the angle to rotate the paddle this tick in degrees
    private int degrees = 0;
    // the total angle the paddle is rotated in degrees
    private int degreesRotated = 0;

    // sets of the original points that we use to construct triangles to find the angle of rotation
    // since there's no KeyDown event in java that lets us keep track of the angle of rotation.
    // We add displacement up/down left/right so that we know the location of the upright position relative
    // to this paddle's current position and orientation.
    private Point2D.Double[] uprightVertices = new Point2D.Double[4];

    public TurnPaddle(int x, int y, int width, int height, Game game, Color color, String id) {
        super(x, y, 0, 0, height, width, color, id);
        this.game = game;
        makeUprightVertices();
    }

    public void tick() {
        // update mid point and vertices
        setX(midPoint.x += velX);
        setY(midPoint.y += velY);

        // update non-rotated vertices
        updateUprightVertices();

        // update the rotated vertices by the angle
        double radians = degrees * (Math.PI / 180);

        // rotate the top left vertex
        double x1 = vertices[0].x - midPoint.x;
        double y1 = vertices[0].y - midPoint.y;

        double x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        double y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        vertices[0].x = x2 + midPoint.x;
        vertices[0].y = y2 + midPoint.y;

        // rotate the bottom left vertex
        x1 = vertices[1].x - midPoint.x;
        y1 = vertices[1].y - midPoint.y;

        x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        vertices[1].x = x2 + midPoint.x;
        vertices[1].y = y2 + midPoint.y;

        // rotate the bottom right vertex
        x1 = vertices[2].x - midPoint.x;
        y1 = vertices[2].y - midPoint.y;

        x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        vertices[2].x = x2 + midPoint.x;
        vertices[2].y = y2 + midPoint.y;

        // rotate the top right vertex
        x1 = vertices[3].x - midPoint.x;
        y1 = vertices[3].y - midPoint.y;

        x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        vertices[3].x = x2 + midPoint.x;
        vertices[3].y = y2 + midPoint.y;

    }

    private void makeUprightVertices() {
        uprightVertices[0] = new Point2D.Double(midPoint.x - (WIDTH / 2), midPoint.y - (HEIGHT / 2));
        uprightVertices[1] = new Point2D.Double(midPoint.x - (WIDTH / 2), midPoint.y + (HEIGHT / 2));
        uprightVertices[2] = new Point2D.Double(midPoint.x + (WIDTH / 2), midPoint.y + (HEIGHT / 2));
        uprightVertices[3] = new Point2D.Double(midPoint.x + (WIDTH / 2), midPoint.y - (HEIGHT / 2));
    }

    private void updateUprightVertices() {
        uprightVertices[0].x = midPoint.x - (WIDTH / 2);
        uprightVertices[0].y = midPoint.y - (HEIGHT / 2);

        uprightVertices[1].x = midPoint.x - (WIDTH / 2);
        uprightVertices[1].y = midPoint.y + (HEIGHT / 2);

        uprightVertices[2].x = midPoint.x + (WIDTH / 2);
        uprightVertices[2].y = midPoint.y + (HEIGHT / 2);

        uprightVertices[3].x = midPoint.x + (WIDTH / 2);
        uprightVertices[3].y = midPoint.y - (HEIGHT / 2);
    }

    public void render(Graphics g) {
        g.setColor(overlap ? Color.YELLOW : color);

        g.fillPolygon(new int[] {(int) vertices[0].x, (int) vertices[1].x, (int) vertices[2].x, (int) vertices[3].x},
                new int[] {(int) vertices[0].y, (int) vertices[1].y, (int) vertices[2].y, (int) vertices[3].y}, 4);
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public int getDegrees() {
        return degrees;
    }

    public void incrementDegrees() {
        degrees -= 20;
    }

    public void decrementDegrees() {
        degrees += 20;
    }

    /**
     * Uses the law of cosines, this paddle's current orientation, and the image of this paddle's upright position
     * relative to this paddle's current position (x,y) to find the angle of rotation in radians.
     * @return the angle of rotation in radians
     */
    public double getDegreesRotated() {
        // law of cosines: c^2 = a^2 + b^2 - 2abcos(theta)

        // get side length c (position of top left vertex to top left vertex of the upright image)
        double c = Math.sqrt(Math.pow(vertices[0].x - uprightVertices[0].x, 2) + Math.pow(vertices[0].y - uprightVertices[0].y, 2));
        //System.out.println(c);

        // get side length a (position of top left vertex to midpoint of the paddle)
        double a = Math.sqrt(Math.pow(vertices[0].x - midPoint.x, 2) + Math.pow(vertices[0].y - midPoint.y, 2));
        //System.out.println(a);

        // get side length b (upright image of top left vertex to midpoint of the paddle)
        double b = Math.sqrt(Math.pow(uprightVertices[0].x - midPoint.x, 2) + Math.pow(uprightVertices[0].y - midPoint.y, 2));
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
