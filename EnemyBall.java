package com.main;
import java.awt.*;
import java.util.TimerTask;

public class EnemyBall extends SimpleGameObject {

    private int speedBoost = 0;

    private String flag;

    public EnemyBall(double x, double y, double width, double height, Color color, String id) {
        super(x, y, 4, -2, height, width, color, id);

        this.WIDTH = width;
        this.HEIGHT = height;

        //Trail trail = new Trail(vertices[0].x, vertices[0].y, Color.YELLOW, 20, 20, 0.05f, handler);
        //handler.addObject(trail.getId(), trail);
    }

    public void tick() {

        setX(midPoint.x += velX);
        setY(midPoint.y += velY);

        // determine how to add the speedboost
        if(speedBoost > 1) {
            if(velX < 0) {
                setX(midPoint.x -= speedBoost);
            }
            else if(velX > 0) {
                setX(midPoint.x += speedBoost);

            }
            if(velY < 0) {
                setY(midPoint.y -= speedBoost);
            }
            else if(velY > 0) {
                setY(midPoint.y += speedBoost);
            }
        }

        collision();

        if(speedBoost < 0)
            speedBoost = 0;
    }

    public void render(Graphics g) {
        g.setColor(overlap ? Color.BLACK : color);
        g.fillPolygon(new int[] {(int) vertices[0].x, (int) vertices[1].x, (int) vertices[2].x, (int) vertices[3].x},
                new int[] {(int) vertices[0].y, (int) vertices[1].y, (int) vertices[2].y, (int) vertices[3].y}, 4);
    }

    private void collision() {

        if(vertices[0].x <= 0) {
            setX(10);
            velX *= -1;
        }

        if(vertices[0].x >= Game.WIDTH) {
            setX(Game.WIDTH - WIDTH);
            velX *= -1;
        }

        if(vertices[0].y >= Game.HEIGHT - HEIGHT - HEIGHT) {
            setY(Game.HEIGHT - HEIGHT - HEIGHT);
            velY *= -1;
        }

        if(vertices[0].y <= 0) {
            setY(HEIGHT * 2);
            velY *= -1;
        }
    }

    public void incrementSpeedboost() {
        speedBoost++;
    }

    public int getSpeedBoost() {
        return speedBoost;
    }

}
