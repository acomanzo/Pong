package com.main;
import java.awt.*;
import java.util.ArrayList;

public class Ball extends SimpleGameObject {

    private int speedBoost = 0;

    private Timer timer;

    private Handler handler;

    private boolean scored = true;

    private Game game;

    public Ball(double x, double y, double width, double height, Timer timer, Handler handler, Game game, Color color, String id) {
        super(x, y, 4, -2, height, width, color, id);

        this.WIDTH = width;
        this.HEIGHT = height;
        this.timer = timer;
        this.handler = handler;
        this.game = game;

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

        // if a player or cpu scored, start a countdown
        // if a player or cpu scored, start a countdown
        if(scored) {
            scored = false;
            if (game.getState().equals("CRAZY_MODE")) {
                game.crazyModeCountdownStarted = false;
            }
            if (game.getState().equals("CLASSIC_MODE")) {
                game.classicModeCountdownStarted = false;
            }
            timer.countdown = 4;
            game.setPauseCrazyModeBall(true);
        }

        // if less than three seconds have passed since the marking of the start stamp, hold the ball in place
        if (game.getPauseCrazyModeBall()) {
            if(velX < 0) {
                setX(midPoint.x + (-velX + speedBoost));
            }
            else if(velX > 0) {
                setX(midPoint.x - (velX + speedBoost));
            }
            if(velY < 0) {
                setY(midPoint.y + (-velY + speedBoost));
            }
            else if(velY > 0) {
                setY(midPoint.y - (velY + speedBoost));
            }
        }

        collision();

        if(speedBoost < 0)
            speedBoost = 0;

        // adds trail to the ball if speed boost is more than five
        /*if(!game.toggleHitBoxes && speedBoost > 5) {
            Trail trail = new Trail(vertices[0].x, vertices[0].y, Color.YELLOW, 20, 20, 0.05f, handler);
            handler.addObject(trail.getId(), trail);
        }*/


    }

    public void render(Graphics g) {
        g.setColor(overlap ? Color.BLACK : color);
        g.fillPolygon(new int[] {(int) vertices[0].x, (int) vertices[1].x, (int) vertices[2].x, (int) vertices[3].x},
                new int[] {(int) vertices[0].y, (int) vertices[1].y, (int) vertices[2].y, (int) vertices[3].y}, 4);
    }

    /**
     * Tests for different things the ball could collide with: a paddle that can't turn, edges of the screen,
     * a paddle that can turn, the cpu.
     */
    private void collision() {

        if(vertices[0].x <= 0) {
            setX(game.WIDTH / 2);
            setY(game.HEIGHT / 2);

            scored = true;
            speedBoost = 0;
            velY = -2;
            velX = 4;
        }

        if(vertices[0].x >= Game.WIDTH) {
            setX(game.WIDTH / 2);
            setY(game.HEIGHT / 2);

            scored = true;
            speedBoost = 0;
            velY = -2;
            velX = 4;
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

    public void setScored(boolean b) {
        scored = b;
    }

    public void incrementSpeedboost() {
        speedBoost++;
    }

    public int getSpeedBoost() {
        return speedBoost;
    }

}
