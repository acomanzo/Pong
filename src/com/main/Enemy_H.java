package com.main;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.TimerTask;

/**
 * Represents an enemy that moves up and down vertically.
 */
public class Enemy_H extends ComplexGameObject {

    private int health;
    private boolean dead;
    private boolean hit;
    private boolean hit_right;
    private boolean hit_left;
    private boolean hit_top;
    private boolean hit_bottom;

    private SpawnerManager spawnerManager;

    public Enemy_H(double x, double y, double velX, double velY, double width, double height, Color color, String id, SpawnerManager spawnerManager) {
        super(x, y, velX, velY, height, width, color, id);
        health = 100;
        dead = false;
        hit = false;
        hit_right = false;
        hit_left = false;
        hit_top = false;
        hit_bottom = false;

        this.spawnerManager = spawnerManager;

        makeTimer();
    }

    public void tick() {

        if (midPoint.x >= 900 || midPoint.x <= 100) {
            velX *= -1;
        }

        setX(midPoint.x += velX);

        if (hit) {
            health = health - 25;
            hit = false;
            System.out.println(health);
        }

        if (health <= 0) {
            dead = true;
        }
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.fillPolygon(new int[] {(int) concaveVertices[0].x, (int) concaveVertices[1].x, (int) concaveVertices[2].x, (int) concaveVertices[3].x,
                (int) concaveVertices[4].x, (int) concaveVertices[5].x}, new int[] {(int) concaveVertices[0].y, (int) concaveVertices[1].y,
                (int) concaveVertices[2].y, (int) concaveVertices[3].y, (int) concaveVertices[4].y, (int) concaveVertices[5].y }, 6);

        /*g.fillPolygon(new int[] {(int) convexVertices[0].x, (int) convexVertices[1].x, (int) convexVertices[2].x},
                    new int[] {(int) convexVertices[0].y, (int) convexVertices[1].y, (int) convexVertices[2].y}, 3);*/
    }

    private void makeTimer() {
        java.util.Timer t = new java.util.Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                spawnerManager.spawnBall(midPoint.x - 10, midPoint.y + 30);
            }
        }, 0, 10000);
    }

    public void makeConcaveVertices() {
        concaveVertices = new Point2D.Double[6];

        Point2D.Double v1 = new Point2D.Double(midPoint.x, midPoint.y - HEIGHT / 2); // tip
        Point2D.Double v2 = new Point2D.Double(midPoint.x - WIDTH / 2, midPoint.y + HEIGHT / 2); // left foot
        Point2D.Double v3 = new Point2D.Double(midPoint.x - (WIDTH / 2) + 20, midPoint.y + HEIGHT / 2);
        Point2D.Double v4 = new Point2D.Double(midPoint.x, midPoint.y - (HEIGHT / 2) + 20); // right under tip
        Point2D.Double v5 = new Point2D.Double(midPoint.x + (WIDTH / 2) - 20, midPoint.y + HEIGHT / 2); // right foot
        Point2D.Double v6 = new Point2D.Double(midPoint.x + WIDTH / 2, midPoint.y + HEIGHT / 2);

        concaveVertices[0] = v1;
        concaveVertices[1] = v2;
        concaveVertices[2] = v3;
        concaveVertices[3] = v4;
        concaveVertices[4] = v5;
        concaveVertices[5] = v6;
    }

    public void makeConvexVertices() {
        convexVertices = new Point2D.Double[3];

        Point2D.Double v1 = new Point2D.Double(midPoint.x, midPoint.y - HEIGHT / 2);
        Point2D.Double v2 = new Point2D.Double(midPoint.x - WIDTH / 2, midPoint.y + HEIGHT / 2);
        Point2D.Double v3 = new Point2D.Double(midPoint.x + WIDTH / 2, midPoint.y + HEIGHT / 2);

        convexVertices[0] = v1;
        convexVertices[1] = v2;
        convexVertices[2] = v3;
    }

    public void resurrect() {
        dead = false;
    }

    public boolean isDead() {
        return dead;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
