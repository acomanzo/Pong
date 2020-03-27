package com.main;

import java.awt.*;
import java.util.ArrayList;

public class Ball extends GameObject {

    private int WIDTH;
    private int HEIGHT;

    private ArrayList<GameObject> gameObjects;

    private int speedBoost = 0;

    private Timer timer;

    private Handler handler;

    private boolean scored = true;

    private Game game;

    public Ball(int x, int y, int width, int height, Timer timer, Handler handler, Game game, ArrayList<GameObject> gameObjects, Color color) {
        super(x, y, 4, -2, color);
        
        this.WIDTH = width;
        this.HEIGHT = height;
        this.timer = timer;
        this.handler = handler;
        this.game = game;
        this.gameObjects = gameObjects;

        makeCoordinates();

    }

    public void makeCoordinates() {
        xCoordinates = new double[]{x, x, x + WIDTH, x + WIDTH};
        yCoordinates = new double[]{y, y + HEIGHT, y + HEIGHT, y};
    }

    public void makeIntegerCoordinates() {

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

        // update yInts
        for(int i = 0; i < yCoordinates.length; i++) {
            yInts[i] = (int) yCoordinates[i];
        }

        // determine how to add the speedboost
        if(speedBoost > 1) {
            if(velX < 0) {
                xCoordinates[0] -= speedBoost;
                xCoordinates[1] -= speedBoost;
                xCoordinates[2] -= speedBoost;
                xCoordinates[3] -= speedBoost;
            }
            else if(velX > 0) {
                xCoordinates[0] += speedBoost;
                xCoordinates[1] += speedBoost;
                xCoordinates[2] += speedBoost;
                xCoordinates[3] += speedBoost;

            }
            if(velY < 0) {
                yCoordinates[0] -= speedBoost;
                yCoordinates[1] -= speedBoost;
                yCoordinates[2] -= speedBoost;
                yCoordinates[3] -= speedBoost;
            }
            else if(velY > 0) {
                yCoordinates[0] += speedBoost;
                yCoordinates[1] += speedBoost;
                yCoordinates[2] += speedBoost;
                yCoordinates[3] += speedBoost;
            }
        }

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
                xCoordinates[0] = xCoordinates[0] + (-velX + speedBoost);
                xCoordinates[1] = xCoordinates[1] + (-velX + speedBoost);
                xCoordinates[2] = xCoordinates[2] + (-velX + speedBoost);
                xCoordinates[3] = xCoordinates[3] + (-velX + speedBoost);
            }
            else if(velX > 0) {
                xCoordinates[0] = xCoordinates[0] - (velX + speedBoost);
                xCoordinates[1] = xCoordinates[1] - (velX + speedBoost);
                xCoordinates[2] = xCoordinates[2] - (velX + speedBoost);
                xCoordinates[3] = xCoordinates[3] - (velX + speedBoost);

            }
            if(velY < 0) {
                yCoordinates[0] = yCoordinates[0] + (-velY + speedBoost);
                yCoordinates[1] = yCoordinates[1] + (-velY + speedBoost);
                yCoordinates[2] = yCoordinates[2] + (-velY + speedBoost);
                yCoordinates[3] = yCoordinates[3] + (-velY + speedBoost);
            }
            else if(velY > 0) {
                yCoordinates[0] = yCoordinates[0] - (velY + speedBoost);
                yCoordinates[1] = yCoordinates[1] - (velY + speedBoost);
                yCoordinates[2] = yCoordinates[2] - (velY + speedBoost);
                yCoordinates[3] = yCoordinates[3] - (velY + speedBoost);
            }
        }

        // adds trail to the ball if speed boost is more than five
        if(!game.toggleHitBoxes && speedBoost > 5)
            handler.addObject(new Trail(xInts[0], yInts[0], Color.YELLOW, 20, 20, 0.05f, handler));

        collision();

        if(speedBoost < 0)
            speedBoost = 0;

        // fill integer arrays so that we can use fillPolygon() in render()
        for(int i = 0; i < xCoordinates.length; i++) {
            xInts[i] = (int) xCoordinates[i];
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) { this.y = y; }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelX () {
        return velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getVelY() {
        return velY;
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.fillPolygon(xInts, yInts, 4);

        if(game.toggleHitBoxes) {
            g.setColor(Color.GREEN);
            g.drawRect(xInts[0], yInts[0], WIDTH, HEIGHT);
        }
    }

    /**
     * Tests for different things the ball could collide with: a paddle that can't turn, edges of the screen,
     * a paddle that can turn, the cpu.
     */
    private void collision() {

        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i) instanceof ClassicPaddle) {
                collideClassicPaddle((ClassicPaddle) gameObjects.get(i));
            }
            else if (gameObjects.get(i) instanceof TurnPaddle) {
                collideTurnPaddle((TurnPaddle) gameObjects.get(i));
            }
            else if (gameObjects.get(i) instanceof CPUPaddle) {
                collideCPUPaddle((CPUPaddle) gameObjects.get(i));
            }
        }

        if(xCoordinates[0] <= 0) {
            xCoordinates[0] = (game.WIDTH / 2) - (WIDTH / 2);
            yCoordinates[0] = (game.HEIGHT / 2) - (HEIGHT / 2);

            xCoordinates[1] = (game.WIDTH / 2) - (WIDTH / 2);
            yCoordinates[1] = (game.HEIGHT / 2) - (WIDTH / 2) + HEIGHT;

            xCoordinates[2] = (game.WIDTH / 2) - (WIDTH / 2) + WIDTH;
            yCoordinates[2] = (game.HEIGHT / 2) - (WIDTH / 2) + HEIGHT;

            xCoordinates[3] = (game.WIDTH / 2) - (WIDTH / 2) + WIDTH;
            yCoordinates[3] = (game.HEIGHT / 2) - (WIDTH / 2);

            game.setCpuScore(game.getCpuScore() + 1);
            scored = true;
            speedBoost = 0;
            velY = -2;
            velX = 4;
        }

        if(xCoordinates[0] >= Game.WIDTH) {
            xCoordinates[0] = (game.WIDTH / 2) - (WIDTH / 2);
            yCoordinates[0] = (game.HEIGHT / 2) - (HEIGHT / 2);

            xCoordinates[1] = (game.WIDTH / 2) - (WIDTH / 2);
            yCoordinates[1] = (game.HEIGHT / 2) - (WIDTH / 2) + HEIGHT;

            xCoordinates[2] = (game.WIDTH / 2) - (WIDTH / 2) + WIDTH;
            yCoordinates[2] = (game.HEIGHT / 2) - (WIDTH / 2) + HEIGHT;

            xCoordinates[3] = (game.WIDTH / 2) - (WIDTH / 2) + WIDTH;
            yCoordinates[3] = (game.HEIGHT / 2) - (WIDTH / 2);

            game.setPlayerScore(game.getPlayerScore() + 1);
            scored = true;
            speedBoost = 0;
            velY = -2;
            velX = 4;
        }

        if(yCoordinates[0] >= Game.HEIGHT - HEIGHT - HEIGHT) {
            yCoordinates[0] = Game.HEIGHT - HEIGHT - HEIGHT; // this prevents ball from getting stuck in bottom of window
            yCoordinates[1] = Game.HEIGHT - HEIGHT;
            yCoordinates[2] = Game.HEIGHT - HEIGHT;
            yCoordinates[3] = Game.HEIGHT - HEIGHT - HEIGHT;
            velY *= -1;
        }

        if(yCoordinates[0] <= 0) {
            yCoordinates[0] = 0;
            yCoordinates[1] = 0 + HEIGHT;
            yCoordinates[2] = 0 + HEIGHT;
            yCoordinates[3] = 0;
            velY *= -1;
        }
    }

    /**
     * Tests if the ball hit a classicPaddle that cannot rotate, and then calculates this Ball's new trajectory.
     * @param classicPaddle The ClassicPaddle in question.
     */
    private void collideClassicPaddle(ClassicPaddle classicPaddle) {
        // the actual top of the classicPaddle
        if(this.yCoordinates[1] > classicPaddle.getY() &&
                this.yCoordinates[1] < classicPaddle.getY() + 10 &&
                this.xCoordinates[1] <= classicPaddle.getX() + classicPaddle.getWIDTH() &&
                this.xCoordinates[2] >= classicPaddle.getX()) {
            this.velY = -this.velY;
            this.yCoordinates[0] = classicPaddle.getY() + classicPaddle.getHEIGHT();
            this.yCoordinates[1] = classicPaddle.getY() + classicPaddle.getHEIGHT() + this.HEIGHT;
            this.yCoordinates[2] = classicPaddle.getY() + classicPaddle.getHEIGHT() + this.HEIGHT;
            this.yCoordinates[3] = classicPaddle.getY() + classicPaddle.getHEIGHT();
            speedBoost++;
        }

        // the actual bottom of the classicPaddle
        else if(this.yCoordinates[0] < classicPaddle.getY() + classicPaddle.getHEIGHT() &&
                this.yCoordinates[0] > classicPaddle.getY() + classicPaddle.getHEIGHT() - 10 &&
                this.xCoordinates[0] <= classicPaddle.getX() + classicPaddle.getWIDTH() &&
                this.xCoordinates[2] >= classicPaddle.getX()) {
            this.velY = -this.velY;
            this.yCoordinates[0] = classicPaddle.getY() + classicPaddle.getHEIGHT();
            this.yCoordinates[1] = classicPaddle.getY() + classicPaddle.getHEIGHT() + this.HEIGHT;
            this.yCoordinates[2] = classicPaddle.getY() + classicPaddle.getHEIGHT() + this.HEIGHT;
            this.yCoordinates[3] = classicPaddle.getY() + classicPaddle.getHEIGHT();
            speedBoost++;
        }

        // middle of classicPaddle
        if((this.xCoordinates[0] < classicPaddle.getX() + classicPaddle.getWIDTH()) &&
                (this.xCoordinates[0] > classicPaddle.getX()) &&
                (this.yCoordinates[0] > (classicPaddle.getRightMidPoint().getY() - 10)) &&
                (this.yCoordinates[0] < (classicPaddle.getRightMidPoint().getY() + 10))) {
            this.xCoordinates[0] = classicPaddle.getX() + classicPaddle.getWIDTH();
            this.xCoordinates[1] = classicPaddle.getX() + classicPaddle.getWIDTH();
            this.xCoordinates[2] = classicPaddle.getX() + classicPaddle.getWIDTH() + this.WIDTH;
            this.xCoordinates[3] = classicPaddle.getX() + classicPaddle.getWIDTH() + this.WIDTH;
            speedBoost++;
            // an attempt at changing the angle of the ball depending on where it hits the classicPaddle. Only works sometimes,
            // often results in xVel or yVel turning to 0
            //this.velX = -Math.round((float) (velX * Math.cos(0)));
            //this.velY = Math.round((float) (velY * Math.sin(0)));
            this.velX *= -1;
        }

        // bottom half of classicPaddle
        else if(this.xCoordinates[0] < classicPaddle.getX() + classicPaddle.getWIDTH() &&
                this.xCoordinates[0] > classicPaddle.getX() &&
                this.yCoordinates[0] > (classicPaddle.getRightMidPoint().getY() + 10) &&
                this.yCoordinates[0] < (classicPaddle.getY() + classicPaddle.getHEIGHT())) {
            this.xCoordinates[0] = classicPaddle.getX() + classicPaddle.getWIDTH();
            this.xCoordinates[1] = classicPaddle.getX() + classicPaddle.getWIDTH();
            this.xCoordinates[2] = classicPaddle.getX() + classicPaddle.getWIDTH() + this.WIDTH;
            this.xCoordinates[3] = classicPaddle.getX() + classicPaddle.getWIDTH() + this.WIDTH;
            speedBoost++;
            //this.velX = -Math.round((float)((velX + 1) * Math.cos((this.y - classicPaddle.getRightMidPoint().getY()) * (Math.PI / 180))));
            //this.velY = Math.round((float)((velY + 1) * Math.sin((this.y - classicPaddle.getRightMidPoint().getY()) * (Math.PI / 180))));
            this.velX *= -1;
        }

        // top half of classicPaddle
        else if(this.xCoordinates[0] < classicPaddle.getX() + classicPaddle.getWIDTH() &&
                this.xCoordinates[0] > classicPaddle.getX() &&
                this.yCoordinates[0] < (classicPaddle.getRightMidPoint().getY() - 10) &&
                this.yCoordinates[1] > (classicPaddle.getY())) {
            this.xCoordinates[0] = classicPaddle.getX() + classicPaddle.getWIDTH();
            this.xCoordinates[1] = classicPaddle.getX() + classicPaddle.getWIDTH();
            this.xCoordinates[2] = classicPaddle.getX() + classicPaddle.getWIDTH() + this.WIDTH;
            this.xCoordinates[3] = classicPaddle.getX() + classicPaddle.getWIDTH() + this.WIDTH;
            speedBoost++;
            //this.velX = -Math.round((float)((velX + 1) * Math.cos(-(this.y - classicPaddle.getRightMidPoint().getY()) * (Math.PI / 180))));
            //this.velY = Math.round((float)((velY + 1) * Math.sin(-(this.y - classicPaddle.getRightMidPoint().getY()) * (Math.PI / 180))));
            this.velX *= -1;
        }
    }

    /**
     * Tests if the ball hit the cpu paddle.
     */
    private void collideCPUPaddle(CPUPaddle cpuPaddle) {

        // top of CPU
        if(this.yCoordinates[1] > cpuPaddle.getY() &&
                this.yCoordinates[1] < cpuPaddle.getY() + 10 &&
                this.xCoordinates[2] <= cpuPaddle.getX() + cpuPaddle.getWIDTH() &&
                this.xCoordinates[0] >= cpuPaddle.getX()) {
            this.velY = -this.velY;
            this.yCoordinates[0] = cpuPaddle.getY() - this.HEIGHT;
            this.yCoordinates[1] = cpuPaddle.getY();
            this.yCoordinates[2] = cpuPaddle.getY();
            this.yCoordinates[3] = cpuPaddle.getY() - this.HEIGHT;
            speedBoost++;
        }

        // bottom of CPU
        else if(this.yCoordinates[0] < cpuPaddle.getY() + cpuPaddle.getHEIGHT() &&
                this.yCoordinates[0] > cpuPaddle.getY() + cpuPaddle.getHEIGHT() - 10 &&
                this.xCoordinates[2] <= cpuPaddle.getX() + cpuPaddle.getWIDTH() &&
                this.xCoordinates[0] >= cpuPaddle.getX()) {
            this.velY = -this.velY;
            this.yCoordinates[0] = cpuPaddle.getY() + cpuPaddle.getHEIGHT() + this.HEIGHT;
            this.yCoordinates[1] = cpuPaddle.getY() + cpuPaddle.getHEIGHT();
            this.yCoordinates[2] = cpuPaddle.getY() + cpuPaddle.getHEIGHT();
            this.yCoordinates[3] = cpuPaddle.getY() + cpuPaddle.getHEIGHT() + this.HEIGHT;
            speedBoost++;
        }

        // middle of CPU
        if((this.xCoordinates[2] > cpuPaddle.getX()) &&
                (this.xCoordinates[2] < cpuPaddle.getX() + cpuPaddle.getWIDTH()) &&
                (this.yCoordinates[0] > (cpuPaddle.getLeftMidPoint().getY() - 10)) &&
                (this.yCoordinates[0] < (cpuPaddle.getLeftMidPoint().getY() + 10))) {
            //this.velX = -Math.round((float) (velX * Math.cos(0)));
            //this.velY = Math.round((float) (velY * Math.sin(0)));
            this.velX *= -1;
            this.xCoordinates[0] = cpuPaddle.getX() - this.WIDTH;
            this.xCoordinates[1] = cpuPaddle.getX() - this.WIDTH;
            this.xCoordinates[2] = cpuPaddle.getX();
            this.xCoordinates[3] = cpuPaddle.getX();
            speedBoost++;
        }

        // bottom half of CPU
        else if(this.xCoordinates[2] > cpuPaddle.getX() &&
                this.xCoordinates[2] < cpuPaddle.getX() + cpuPaddle.getWIDTH() &&
                this.yCoordinates[0] > (cpuPaddle.getLeftMidPoint().getY() + 10) &&
                this.yCoordinates[0] < (cpuPaddle.getY() + cpuPaddle.getHEIGHT())) {
            //this.velX = -Math.round((float)((velX + 1) * Math.cos((this.y - cpuPaddle.getLeftMidPoint().getY()) * (Math.PI / 180))));
            //this.velY = Math.round((float)((velY + 1) * Math.sin((this.y - cpuPaddle.getLeftMidPoint().getY()) * (Math.PI / 180))));
            this.velX *= -1;
            this.xCoordinates[0] = cpuPaddle.getX() - this.WIDTH;
            this.xCoordinates[1] = cpuPaddle.getX() - this.WIDTH;
            this.xCoordinates[2] = cpuPaddle.getX();
            this.xCoordinates[3] = cpuPaddle.getX();
            speedBoost++;
        }

        // top half of CPU
        else if(this.xCoordinates[2] > cpuPaddle.getX() &&
                this.xCoordinates[2] < cpuPaddle.getX() + cpuPaddle.getWIDTH() &&
                this.yCoordinates[0] < (cpuPaddle.getLeftMidPoint().getY() - 10) &&
                this.yCoordinates[1] > (cpuPaddle.getY())) {
            //this.velX = -Math.round((float)((velX + 1) * Math.cos(-(this.y - cpuPaddle.getLeftMidPoint().getY()) * (Math.PI / 180))));
            //this.velY = Math.round((float)((velY + 1) * Math.sin(-(this.y - cpuPaddle.getLeftMidPoint().getY()) * (Math.PI / 180))));
            this.velX *= -1;
            this.xCoordinates[0] = cpuPaddle.getX() - this.WIDTH;
            this.xCoordinates[1] = cpuPaddle.getX() - this.WIDTH;
            this.xCoordinates[2] = cpuPaddle.getX();
            this.xCoordinates[3] = cpuPaddle.getX();
            speedBoost++;
        }
    }

    /**
     * Tests if this ball hit a paddle that can rotate. Starts by calling a helper function intersects(). If some of
     * the ball and paddle overlap, then determines how to calculate a trajectory by figuring out which part of the
     * paddle the ball hit. Rotates the paddle by the amount of degrees rotated back to an "upright" position and
     * rotates the ball the same way. Calculates, then reverts the ball and paddle to their original orientations.
     * @param turnPaddle The ClassicPaddle in question.
     */
    private void collideTurnPaddle(TurnPaddle turnPaddle) {
        if(this.intersects(turnPaddle)) {
            System.out.println("hit");

            // rotate the paddle and ball by the paddle's current angle of rotation so that they're "neutral"
            double radians = turnPaddle.getDegreesRotated();
            //double radians = Math.PI;
            //System.out.println(radians);
            double[] paddleXCoordinates = turnPaddle.getXCoordinates();
            double[] paddleYCoordinates = turnPaddle.getYCoordinates();
            double midpointX = turnPaddle.getMidpointX();
            double midpointY = turnPaddle.getMidpointY();
            //System.out.println(midpointX);
            //System.out.println(midpointY);

            // rotate the top left vertex
            double x1 = paddleXCoordinates[0] - midpointX;
            double y1 = paddleYCoordinates[0] - midpointY;

            double x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            double y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddleXCoordinates[0] = x2 + midpointX;
            paddleYCoordinates[0] = y2 + midpointY;

            // rotate the bottom left vertex
            x1 = paddleXCoordinates[1] - midpointX;
            y1 = paddleYCoordinates[1] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddleXCoordinates[1] = x2 + midpointX;
            paddleYCoordinates[1] = y2 + midpointY;

            // rotate the bottom right vertex
            x1 = paddleXCoordinates[2] - midpointX;
            y1 = paddleYCoordinates[2] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddleXCoordinates[2] = x2 + midpointX;
            paddleYCoordinates[2] = y2 + midpointY;

            // rotate the top right vertex
            x1 = paddleXCoordinates[3] - midpointX;
            y1 = paddleYCoordinates[3] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddleXCoordinates[3] = x2 + midpointX;
            paddleYCoordinates[3] = y2 + midpointY;


            // rotate the ball:
            // rotate the top left vertex
            x1 = this.xCoordinates[0] - midpointX;
            y1 = this.yCoordinates[0] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xCoordinates[0] = x2 + midpointX;
            this.yCoordinates[0] = y2 + midpointY;

            // rotate the bottom left vertex
            x1 = this.xCoordinates[1] - midpointX;
            y1 = this.yCoordinates[1] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xCoordinates[1] = x2 + midpointX;
            this.yCoordinates[1] = y2 + midpointY;

            // rotate the bottom right vertex
            x1 = this.xCoordinates[2] - midpointX;
            y1 = this.yCoordinates[2] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xCoordinates[2] = x2 + midpointX;
            this.yCoordinates[2] = y2 + midpointY;

            // rotate the top right vertex
            x1 = this.xCoordinates[3] - midpointX;
            y1 = this.yCoordinates[3] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xCoordinates[3] = x2 + midpointX;
            this.yCoordinates[3] = y2 + midpointY;


            // do the math:
            // "head" of the paddle:
            // case 1.1: bottom of the ball hits the head
            if(this.yCoordinates[1] > paddleYCoordinates[0] &&
                    this.yCoordinates[1] < paddleYCoordinates[0] + 10 &&
                    this.xCoordinates[1] <= paddleXCoordinates[2] &&
                    this.xCoordinates[2] >= paddleXCoordinates[0]) {
                this.velY = -this.velY;
                this.yCoordinates[0] = paddleYCoordinates[0] + this.HEIGHT;
                this.yCoordinates[1] = paddleYCoordinates[0];
                this.yCoordinates[2] = paddleYCoordinates[0];
                this.yCoordinates[3] = paddleYCoordinates[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("bottom to head");
            }
            // case 1.2: right side of the ball hits the head
            else if (this.yCoordinates[2] > paddleYCoordinates[0] &&
                    this.yCoordinates[2] < paddleYCoordinates[0] + 10 &&
                    this.xCoordinates[3] <= paddleXCoordinates[2] &&
                    this.xCoordinates[2] >= paddleXCoordinates[0]) {
                this.velY = -this.velY;
                this.velX = -this.velX;
                this.yCoordinates[2] = paddleYCoordinates[0];
                this.yCoordinates[3] = paddleYCoordinates[0];
                this.yCoordinates[0] = paddleYCoordinates[0] + this.HEIGHT;
                this.yCoordinates[1] = paddleYCoordinates[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("right to head");
            }
            // case 1.3: top of the ball hits the head
            else if (this.yCoordinates[0] > paddleYCoordinates[0] &&
                    this.yCoordinates[0] < paddleYCoordinates[0] + 10 &&
                    this.xCoordinates[0] <= paddleXCoordinates[2] &&
                    this.xCoordinates[3] >= paddleXCoordinates[0]) {
                this.velY = -this.velY;
                this.yCoordinates[0] = paddleYCoordinates[0];
                this.yCoordinates[3] = paddleYCoordinates[0];
                this.yCoordinates[1] = paddleYCoordinates[0] + this.HEIGHT;
                this.yCoordinates[2] = paddleYCoordinates[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("top to head");
            }
            // case 1.4: left side of the ball hits the head
            else if (this.yCoordinates[0] > paddleYCoordinates[0] &&
                    this.yCoordinates[0] < paddleYCoordinates[0] + 10 &&
                    this.xCoordinates[1] <= paddleXCoordinates[2] &&
                    this.xCoordinates[0] >= paddleXCoordinates[0]) {
                this.velY = -this.velY;
                this.velX = -this.velX;
                this.yCoordinates[0] = paddleYCoordinates[0];
                this.yCoordinates[1] = paddleYCoordinates[0];
                this.yCoordinates[2] = paddleYCoordinates[0] + this.HEIGHT;
                this.yCoordinates[3] = paddleYCoordinates[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("left to head");
            }

            // "tail" of the paddle
            // case 2.1: top of the ball hits the tail
            else if(this.yCoordinates[0] < paddleYCoordinates[0] + turnPaddle.getHEIGHT() &&
                    this.yCoordinates[0] > paddleYCoordinates[0] + turnPaddle.getHEIGHT() - 10 &&
                    this.xCoordinates[0] <= paddleXCoordinates[0] + turnPaddle.getWIDTH() &&
                    this.xCoordinates[2] >= paddleXCoordinates[0]) {
                this.velY = -this.velY;
                this.yCoordinates[0] = paddleYCoordinates[0] + turnPaddle.getHEIGHT();
                this.yCoordinates[1] = paddleYCoordinates[0] + turnPaddle.getHEIGHT() + this.HEIGHT;
                this.yCoordinates[2] = paddleYCoordinates[0] + turnPaddle.getHEIGHT() + this.HEIGHT;
                this.yCoordinates[3] = paddleYCoordinates[0] + turnPaddle.getHEIGHT();
                speedBoost++;
                System.out.println("top to tail");
            }
            // case 2.2: right side of the ball hits the tail
            else if (this.yCoordinates[2] > paddleYCoordinates[0] &&
                    this.yCoordinates[2] < paddleYCoordinates[0] + 10 &&
                    this.xCoordinates[2] <= paddleXCoordinates[2] &&
                    this.xCoordinates[3] >= paddleXCoordinates[0]) {
                this.velY = -this.velY;
                this.velX = -this.velX;
                this.yCoordinates[2] = paddleYCoordinates[0];
                this.yCoordinates[3] = paddleYCoordinates[0];
                this.yCoordinates[0] = paddleYCoordinates[0] + this.HEIGHT;
                this.yCoordinates[1] = paddleYCoordinates[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("right to head");
            }
            // case 2.3: bottom of the ball hits the tail

            // case 2.4: left side of the ball hits the tail
            else if (this.yCoordinates[0] < paddleYCoordinates[1] &&
                    this.yCoordinates[0] > paddleYCoordinates[1] - 10 &&
                    this.xCoordinates[0] <= paddleXCoordinates[2] &&
                    this.xCoordinates[1] >= paddleXCoordinates[1]) {
                this.velY = -this.velY;
                this.velX = -this.velX;
                this.yCoordinates[0] = paddleYCoordinates[1];
                this.yCoordinates[1] = paddleYCoordinates[1];
                this.yCoordinates[2] = paddleYCoordinates[1] + this.HEIGHT;
                this.yCoordinates[3] = paddleYCoordinates[1] + this.HEIGHT;
                speedBoost++;
                System.out.println("left to head");
            }

            // case 3.1: top of ball hits left side of paddle
            else if (this.xCoordinates[0] > paddleXCoordinates[0] && this.xCoordinates[0] < (paddleXCoordinates[0] + turnPaddle.getWIDTH()) &&
                    this.yCoordinates[0] > paddleYCoordinates[0] && this.yCoordinates[0] < (paddleYCoordinates[0] + turnPaddle.getHEIGHT())) {
                this.velY = -this.velY;
                System.out.println("Top to left");
            }

            // case 3.2: right side of the ball hits left side of paddle
            // case 3.2.1: middle of paddle
            if((this.xCoordinates[0] + this.WIDTH < paddleXCoordinates[0] + turnPaddle.getWIDTH()) &&
                    (this.xCoordinates[0] + this.WIDTH > paddleXCoordinates[0]) &&
                    (this.yCoordinates[0] > (((paddleYCoordinates[0] + paddleYCoordinates[1]) / 2 ) - 10)) &&
                    (this.yCoordinates[0] < (((paddleYCoordinates[0] + paddleYCoordinates[1]) / 2 ) + 10))) {
                //this.velX = -Math.round((float) (velX * Math.cos(0)));
                //this.velY = Math.round((float) (velY * Math.sin(0)));
                this.velX *= -1;
                this.xCoordinates[0] = paddleXCoordinates[0] - this.WIDTH;
                this.xCoordinates[1] = paddleXCoordinates[0] - this.WIDTH;
                this.xCoordinates[2] = paddleXCoordinates[0];
                this.xCoordinates[3] = paddleXCoordinates[0];
                speedBoost++;
                System.out.println("Middle");
            }

            // case 3.2.2: bottom half of paddle
            else if(this.xCoordinates[0] + this.WIDTH > paddleXCoordinates[0] &&
                    this.xCoordinates[0] + this.WIDTH < paddleXCoordinates[0] + turnPaddle.getWIDTH() &&
                    this.yCoordinates[0] > (((paddleYCoordinates[0] + paddleYCoordinates[1]) / 2 ) + 10) &&
                    this.yCoordinates[0] < (paddleYCoordinates[0] + turnPaddle.getHEIGHT())) {
                //this.velX = -Math.round((float)((velX + 1) * Math.cos((this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
                //this.velY = Math.round((float)((velY + 1) * Math.sin((this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
                this.velX *= -1;
                this.xCoordinates[0] = paddleXCoordinates[0] - this.WIDTH;
                this.xCoordinates[1] = paddleXCoordinates[0] - this.WIDTH;
                this.xCoordinates[2] = paddleXCoordinates[0];
                this.xCoordinates[3] = paddleXCoordinates[0];
                speedBoost++;
                System.out.println("bottom");
            }

            // case 3.2.3: top half of paddle
            else if(this.xCoordinates[0] + this.WIDTH < paddleXCoordinates[0] + turnPaddle.getWIDTH() &&
                    this.xCoordinates[0] + this.WIDTH > paddleXCoordinates[0] &&
                    this.yCoordinates[0] < (((paddleYCoordinates[0] + paddleYCoordinates[1]) / 2 ) - 10) &&
                    this.yCoordinates[1] > (paddleYCoordinates[0])) {
                //this.velX = -Math.round((float)((velX + 1) * Math.cos(-(this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
                //this.velY = Math.round((float)((velY + 1) * Math.sin(-(this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
                this.velX *= -1;
                this.xCoordinates[0] = paddleXCoordinates[0] - this.WIDTH;
                this.xCoordinates[1] = paddleXCoordinates[0] - this.WIDTH;
                this.xCoordinates[2] = paddleXCoordinates[0];
                this.xCoordinates[3] = paddleXCoordinates[0];
                speedBoost++;
                System.out.println("Top");
            }

            // case 3.3: bottom of the ball hits the left side of paddle
            else if (this.xCoordinates[1] > paddleXCoordinates[0] && this.xCoordinates[1] < (paddleXCoordinates[0] + turnPaddle.getWIDTH()) &&
                    this.yCoordinates[2] > paddleYCoordinates[0] && this.yCoordinates[1] < (paddleYCoordinates[0] + turnPaddle.getHEIGHT())) {
                this.velY = -this.velY;
                System.out.println("bottom to left");
            }

            // case 3.4: left side of the ball hits the left side of paddle
            else if (this.xCoordinates[0] > paddleXCoordinates[0] && this.xCoordinates[0] < (paddleXCoordinates[0] + turnPaddle.getWIDTH()) &&
                    this.yCoordinates[0] > paddleYCoordinates[0] && this.yCoordinates[0] < (paddleYCoordinates[0] + turnPaddle.getHEIGHT())) {
                this.velX = -this.velX;
                System.out.println("left to left");
            }

            // case 4.1: top of ball hits right side of paddle
            else if (this.xCoordinates[0] < paddleXCoordinates[0] + turnPaddle.getWIDTH() && this.xCoordinates[0] > paddleXCoordinates[0] &&
                    this.yCoordinates[3] > paddleYCoordinates[0] && this.yCoordinates[0] < paddleYCoordinates[0] + turnPaddle.getHEIGHT()) {
                this.velY = -this.velY;
                System.out.println("Top to right");
            }

            // case 4.2: right side of the ball hits right side of paddle
            else if (this.xCoordinates[2] < paddleXCoordinates[0] + turnPaddle.getWIDTH() && this.xCoordinates[2] > paddleXCoordinates[0] &&
                    this.yCoordinates[2] > paddleYCoordinates[0] && this.yCoordinates[2] < paddleYCoordinates[0] + turnPaddle.getHEIGHT()) {
                this.velX = -this.velX;
                System.out.println("Right to right");
            }

            // case 4.3: bottom of the ball hits the right side of paddle
            else if (this.xCoordinates[1] < paddleXCoordinates[0] + turnPaddle.getWIDTH() && this.xCoordinates[1] > paddleXCoordinates[0] &&
                    this.yCoordinates[1] > paddleYCoordinates[0] && this.yCoordinates[1] < paddleYCoordinates[0] + turnPaddle.getHEIGHT()) {
                this.velY = -this.velY;
                System.out.println("Bottom to right");
            }

            // case 4.4: left side of the ball hits the right side of paddle
            else if (this.xCoordinates[0] < paddleXCoordinates[0] + turnPaddle.getWIDTH() && this.xCoordinates[0] > paddleXCoordinates[0] &&
                    this.yCoordinates[0] > paddleYCoordinates[0] && this.yCoordinates[1] < paddleYCoordinates[0] + turnPaddle.getHEIGHT()) {
                this.velX = -this.velX;
                System.out.println("left to right");
            }

            // put the everything back to where it should be
            // negate the angle
            radians *= -1;
            // rotate the top left vertex
            x1 = paddleXCoordinates[0] - midpointX;
            y1 = paddleYCoordinates[0] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddleXCoordinates[0] = x2 + midpointX;
            paddleYCoordinates[0] = y2 + midpointY;

            // rotate the bottom left vertex
            x1 = paddleXCoordinates[1] - midpointX;
            y1 = paddleYCoordinates[1] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddleXCoordinates[1] = x2 + midpointX;
            paddleYCoordinates[1] = y2 + midpointY;

            // rotate the bottom right vertex
            x1 = paddleXCoordinates[2] - midpointX;
            y1 = paddleYCoordinates[2] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddleXCoordinates[2] = x2 + midpointX;
            paddleYCoordinates[2] = y2 + midpointY;

            // rotate the top right vertex
            x1 = paddleXCoordinates[3] - midpointX;
            y1 = paddleYCoordinates[3] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddleXCoordinates[3] = x2 + midpointX;
            paddleYCoordinates[3] = y2 + midpointY;


            // rotate the ball:
            // rotate the top left vertex
            x1 = this.xCoordinates[0] - midpointX;
            y1 = this.yCoordinates[0] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xCoordinates[0] = x2 + midpointX;
            this.yCoordinates[0] = y2 + midpointY;

            // rotate the bottom left vertex
            x1 = this.xCoordinates[1] - midpointX;
            y1 = this.yCoordinates[1] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xCoordinates[1] = x2 + midpointX;
            this.yCoordinates[1] = y2 + midpointY;

            // rotate the bottom right vertex
            x1 = this.xCoordinates[2] - midpointX;
            y1 = this.yCoordinates[2] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xCoordinates[2] = x2 + midpointX;
            this.yCoordinates[2] = y2 + midpointY;

            // rotate the top right vertex
            x1 = this.xCoordinates[3] - midpointX;
            y1 = this.yCoordinates[3] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xCoordinates[3] = x2 + midpointX;
            this.yCoordinates[3] = y2 + midpointY;
            //*/

        }
    }

    /**
     * Tests if a point is within the bounds of this Ball.
     * @param x the x-coordinate of a point
     * @param y the y-coordinate of a point
     * @return true if the point generated by x and y is within the bounds of this Ball.
     */
    public boolean contains(double x, double y) {
        if(x >= this.xCoordinates[0] && x <= this.xCoordinates[0] + this.WIDTH && y >= this.yCoordinates[0] && y <= this.yCoordinates[0] + this.HEIGHT) {
            return true;
        }
        return false;
    }

    /**
     * Checks if this Ball is intersecting a TurnPaddle at several points. If a point of the TurnPaddle
     * is within the bounds of the Ball, they are considered intersecting.
     * @param paddle the TurnPaddle this Ball might intersect.
     * @return true if a point on the TurnPaddle is within the bounds of this Ball.
     */
    public boolean intersects(TurnPaddle paddle) {
        double xCoordinates[] = paddle.getXCoordinates();
        double yCoordinates[] = paddle.getYCoordinates();

        double x, y;

        // relative to the neutral upright position...
        // is the ball overlapping the top left corner?
        if(this.contains(xCoordinates[0], yCoordinates[0])) {
            return true;
        }

        // is the ball overlapping halfway between the left midpoint and the top left vertex?
        x = (((xCoordinates[0] + xCoordinates[1]) / 2) + xCoordinates[0]) / 2;
        y = (((yCoordinates[0] + yCoordinates[1]) / 2) + yCoordinates[0]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping the midpoint down the left side?
        x = (xCoordinates[0] + xCoordinates[1]) / 2;
        y = (yCoordinates[0] + yCoordinates[1]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping halfway between the left midpoint and the bottom left vertex?
        x = (((xCoordinates[0] + xCoordinates[1]) / 2) + xCoordinates[1]) / 2;
        y = (((yCoordinates[0] + yCoordinates[1]) / 2) + yCoordinates[1]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping the bottom left vertex?
        if(this.contains(xCoordinates[1], yCoordinates[1])) {
            return true;
        }

        // is the ball overlapping the bottom right vertex?
        if(this.contains(xCoordinates[2], yCoordinates[2])) {
            return true;
        }

        // is the ball overlapping halfway between the right side midpoint and the bottom right vertex?
        x = (((xCoordinates[2] + xCoordinates[3]) / 2) + xCoordinates[2]) / 2;
        y = (((xCoordinates[2] + xCoordinates[3]) / 2) + xCoordinates[2]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping the midpoint on the right side?
        x = (xCoordinates[2] + xCoordinates[3]) / 2;
        y = (yCoordinates[2] + yCoordinates[3]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping halfway between the right side midpoint and the top right vertex?
        x = (((xCoordinates[2] + xCoordinates[3]) / 2) + xCoordinates[3]) / 2;
        y = (((xCoordinates[2] + xCoordinates[3]) / 2) + xCoordinates[3]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping the top right vertex?
        if(this.contains(xCoordinates[3], yCoordinates[3])) {
            return true;
        }

        return false;
    }

    public void setScored(boolean b) {
        scored = b;
    }

    /**
     * To be called by Enemy_H
     */
    protected void setXCoordinates(double [] x_points) {
        this.xCoordinates = x_points;
    }

    /**
     * To be called by Enemy_H
     */
    protected void setYCoordinates(double[] y_points) {
        this.yCoordinates = y_points;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }
}
