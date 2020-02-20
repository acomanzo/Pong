package com.main;

import java.awt.*;

public class Ball extends GameObject {

    private final int HEIGHT = 20, WIDTH = 20;

    private int velX = 4;
    private int velY = -2;

    private Paddle player1;
    private TurnPaddle turnPaddle;

    private int speedBoost = 0;

    private CPUPaddle cpuPaddle;

    private Timer timer;

    private Handler handler;

    private boolean scored = true;

    private long startStamp = 0;

    private Game game;

    // arrays we use to not lose data precision. [0] = top left, [1] = bottom left, [2] = bottom right, [3] = top right
    private double[] xPoints;
    private double[] yPoints;

    // arrays we use with the native fillPolygon() class because it won't accept double.
    int[] xInts = new int[4];
    int[] yInts = new int[4];

    public Ball(int x, int y, Paddle player1, TurnPaddle turnPaddle, Timer timer, Handler handler, Game game, CPUPaddle cpuPaddle) {
        super(x, y);

        this.player1 = player1;
        this.turnPaddle = turnPaddle;
        this.timer = timer;
        this.handler = handler;
        this.game = game;
        this.cpuPaddle = cpuPaddle;

        xPoints = new double[]{x, x, x + WIDTH, x + WIDTH};
        yPoints = new double[]{y, y + HEIGHT, y + HEIGHT, y};
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

        // update yInts
        for(int i = 0; i < yPoints.length; i++) {
            yInts[i] = (int) yPoints[i];
        }

        // determine how to add the speedboost
        if(speedBoost > 1) {
            if(velX < 0) {
                xPoints[0] -= speedBoost;
                xPoints[1] -= speedBoost;
                xPoints[2] -= speedBoost;
                xPoints[3] -= speedBoost;
            }
            else if(velX > 0) {
                xPoints[0] += speedBoost;
                xPoints[1] += speedBoost;
                xPoints[2] += speedBoost;
                xPoints[3] += speedBoost;

            }
            if(velY < 0) {
                yPoints[0] -= speedBoost;
                yPoints[1] -= speedBoost;
                yPoints[2] -= speedBoost;
                yPoints[3] -= speedBoost;
            }
            else if(velY > 0) {
                yPoints[0] += speedBoost;
                yPoints[1] += speedBoost;
                yPoints[2] += speedBoost;
                yPoints[3] += speedBoost;
            }
        }

        long time = timer.getElapsedSeconds();

        // if a player or cpu scored, start a countdown
        if(scored == true) {
            startStamp = timer.getElapsedSeconds();
            scored = false;
            game.countdownStarted = false;
            timer.countdown = 4;
        }
        // if less than four seconds have passed since the marking of the start stamp, hold the ball in place
        if (time - startStamp < 4) {
            if(velX < 0) {
                xPoints[0] = xPoints[0] + (-velX + speedBoost);
                xPoints[1] = xPoints[1] + (-velX + speedBoost);
                xPoints[2] = xPoints[2] + (-velX + speedBoost);
                xPoints[3] = xPoints[3] + (-velX + speedBoost);
            }
            else if(velX > 0) {
                xPoints[0] = xPoints[0] - (velX + speedBoost);
                xPoints[1] = xPoints[1] - (velX + speedBoost);
                xPoints[2] = xPoints[2] - (velX + speedBoost);
                xPoints[3] = xPoints[3] - (velX + speedBoost);

            }
            if(velY < 0) {
                yPoints[0] = yPoints[0] + (-velY + speedBoost);
                yPoints[1] = yPoints[1] + (-velY + speedBoost);
                yPoints[2] = yPoints[2] + (-velY + speedBoost);
                yPoints[3] = yPoints[3] + (-velY + speedBoost);
            }
            else if(velY > 0) {
                yPoints[0] = yPoints[0] - (velY + speedBoost);
                yPoints[1] = yPoints[1] - (velY + speedBoost);
                yPoints[2] = yPoints[2] - (velY + speedBoost);
                yPoints[3] = yPoints[3] - (velY + speedBoost);
            }
        }

        // adds trail to the ball if speed boost is more than five
        if(!game.toggleHitboxes && speedBoost > 5)
            handler.addObject(new Trail(xInts[0], yInts[0], Color.YELLOW, 20, 20, 0.05f, handler));

        collision();

        if(speedBoost < 0)
            speedBoost = 0;

        // fill integer arrays so that we can use fillPolygon() in render()
        for(int i = 0; i < xPoints.length; i++) {
            xInts[i] = (int) xPoints[i];
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
        g.setColor(Color.ORANGE);
        g.fillPolygon(xInts, yInts, 4);

        if(game.toggleHitboxes) {
            g.setColor(Color.GREEN);
            g.drawRect(xInts[0], yInts[0], WIDTH, HEIGHT);
        }
    }

    /**
     * Tests for different things the ball could collide with: a paddle that can't turn, edges of the screen,
     * a paddle that can turn, the cpu.
     */
    private void collision() {

        reflectOffRegPaddle(player1);

        reflectOffRegCpuPaddle();

        if(turnPaddle != null) {
            reflectOffTurnPaddle(turnPaddle);
        }

        if(xPoints[0] <= 0) {
            xPoints[0] = (game.WIDTH / 2) - (WIDTH / 2);
            yPoints[0] = (game.HEIGHT / 2) - (HEIGHT / 2);

            xPoints[1] = (game.WIDTH / 2) - (WIDTH / 2);
            yPoints[1] = (game.HEIGHT / 2) - (WIDTH / 2) + HEIGHT;

            xPoints[2] = (game.WIDTH / 2) - (WIDTH / 2) + WIDTH;
            yPoints[2] = (game.HEIGHT / 2) - (WIDTH / 2) + HEIGHT;

            xPoints[3] = (game.WIDTH / 2) - (WIDTH / 2) + WIDTH;
            yPoints[3] = (game.HEIGHT / 2) - (WIDTH / 2);

            game.setPlayer2Score(game.getPlayer2Score() + 1);
            scored = true;
            speedBoost = 0;
            velY = -2;
            velX = 4;
        }

        if(xPoints[0] >= Game.WIDTH) {
            xPoints[0] = (game.WIDTH / 2) - (WIDTH / 2);
            yPoints[0] = (game.HEIGHT / 2) - (HEIGHT / 2);

            xPoints[1] = (game.WIDTH / 2) - (WIDTH / 2);
            yPoints[1] = (game.HEIGHT / 2) - (WIDTH / 2) + HEIGHT;

            xPoints[2] = (game.WIDTH / 2) - (WIDTH / 2) + WIDTH;
            yPoints[2] = (game.HEIGHT / 2) - (WIDTH / 2) + HEIGHT;

            xPoints[3] = (game.WIDTH / 2) - (WIDTH / 2) + WIDTH;
            yPoints[3] = (game.HEIGHT / 2) - (WIDTH / 2);

            game.setPlayer1Score(game.getPlayer1Score() + 1);
            scored = true;
            speedBoost = 0;
            velY = -2;
            velX = 4;
        }

        if(yPoints[0] >= Game.HEIGHT - HEIGHT - HEIGHT) {
            yPoints[0] = Game.HEIGHT - HEIGHT - HEIGHT; // this prevents ball from getting stuck in bottom of window
            yPoints[1] = Game.HEIGHT - HEIGHT;
            yPoints[2] = Game.HEIGHT - HEIGHT;
            yPoints[3] = Game.HEIGHT - HEIGHT - HEIGHT;
            velY *= -1;
        }

        if(yPoints[0] <= 0) {
            yPoints[0] = 0;
            yPoints[1] = 0 + HEIGHT;
            yPoints[2] = 0 + HEIGHT;
            yPoints[3] = 0;
            velY *= -1;
        }
    }

    /**
     * Tests if the ball hit a paddle that cannot rotate, and then calculates this Ball's new trajectory.
     * @param paddle The Paddle in question.
     */
    private void reflectOffRegPaddle(Paddle paddle) {
        // the actual top of the paddle
        if(this.yPoints[1] > paddle.getY() &&
                this.yPoints[1] < paddle.getY() + 10 &&
                this.xPoints[1] <= paddle.getX() + paddle.getWIDTH() &&
                this.xPoints[2] >= paddle.getX()) {
            this.velY = -this.velY;
            this.yPoints[0] = paddle.getY() + paddle.getHEIGHT();
            this.yPoints[1] = paddle.getY() + paddle.getHEIGHT() + this.HEIGHT;
            this.yPoints[2] = paddle.getY() + paddle.getHEIGHT() + this.HEIGHT;
            this.yPoints[3] = paddle.getY() + paddle.getHEIGHT();
            speedBoost++;
        }

        // the actual bottom of the paddle
        else if(this.yPoints[0] < paddle.getY() + paddle.getHEIGHT() &&
                this.yPoints[0] > paddle.getY() + paddle.getHEIGHT() - 10 &&
                this.xPoints[0] <= paddle.getX() + paddle.getWIDTH() &&
                this.xPoints[2] >= paddle.getX()) {
            this.velY = -this.velY;
            this.yPoints[0] = paddle.getY() + paddle.getHEIGHT();
            this.yPoints[1] = paddle.getY() + paddle.getHEIGHT() + this.HEIGHT;
            this.yPoints[2] = paddle.getY() + paddle.getHEIGHT() + this.HEIGHT;
            this.yPoints[3] = paddle.getY() + paddle.getHEIGHT();
            speedBoost++;
        }

        // middle of paddle
        if((this.xPoints[0] < paddle.getX() + paddle.getWIDTH()) &&
                (this.xPoints[0] > paddle.getX()) &&
                (this.yPoints[0] > (paddle.getRightMidPoint().getY() - 10)) &&
                (this.yPoints[0] < (paddle.getRightMidPoint().getY() + 10))) {
            this.xPoints[0] = paddle.getX() + paddle.getWIDTH();
            this.xPoints[1] = paddle.getX() + paddle.getWIDTH();
            this.xPoints[2] = paddle.getX() + paddle.getWIDTH() + this.WIDTH;
            this.xPoints[3] = paddle.getX() + paddle.getWIDTH() + this.WIDTH;
            speedBoost++;
            // an attempt at changing the angle of the ball depending on where it hits the paddle. Only works sometimes,
            // often results in xVel or yVel turning to 0
            //this.velX = -Math.round((float) (velX * Math.cos(0)));
            //this.velY = Math.round((float) (velY * Math.sin(0)));
            this.velX *= -1;
        }

        // bottom half of paddle
        else if(this.xPoints[0] < paddle.getX() + paddle.getWIDTH() &&
                this.xPoints[0] > paddle.getX() &&
                this.yPoints[0] > (paddle.getRightMidPoint().getY() + 10) &&
                this.yPoints[0] < (paddle.getY() + paddle.getHEIGHT())) {
            this.xPoints[0] = paddle.getX() + paddle.getWIDTH();
            this.xPoints[1] = paddle.getX() + paddle.getWIDTH();
            this.xPoints[2] = paddle.getX() + paddle.getWIDTH() + this.WIDTH;
            this.xPoints[3] = paddle.getX() + paddle.getWIDTH() + this.WIDTH;
            speedBoost++;
            //this.velX = -Math.round((float)((velX + 1) * Math.cos((this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
            //this.velY = Math.round((float)((velY + 1) * Math.sin((this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
            this.velX *= -1;
        }

        // top half of paddle
        else if(this.xPoints[0] < paddle.getX() + paddle.getWIDTH() &&
                this.xPoints[0] > paddle.getX() &&
                this.yPoints[0] < (paddle.getRightMidPoint().getY() - 10) &&
                this.yPoints[1] > (paddle.getY())) {
            this.xPoints[0] = paddle.getX() + paddle.getWIDTH();
            this.xPoints[1] = paddle.getX() + paddle.getWIDTH();
            this.xPoints[2] = paddle.getX() + paddle.getWIDTH() + this.WIDTH;
            this.xPoints[3] = paddle.getX() + paddle.getWIDTH() + this.WIDTH;
            speedBoost++;
            //this.velX = -Math.round((float)((velX + 1) * Math.cos(-(this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
            //this.velY = Math.round((float)((velY + 1) * Math.sin(-(this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
            this.velX *= -1;
        }
    }

    /**
     * Tests if the ball hit the cpu paddle.
     */
    private void reflectOffRegCpuPaddle() {

        // top of CPU
        if(this.yPoints[1] > cpuPaddle.getY() &&
                this.yPoints[1] < cpuPaddle.getY() + 10 &&
                this.xPoints[2] <= cpuPaddle.getX() + cpuPaddle.getWIDTH() &&
                this.xPoints[0] >= cpuPaddle.getX()) {
            this.velY = -this.velY;
            this.yPoints[0] = cpuPaddle.getY() - this.HEIGHT;
            this.yPoints[1] = cpuPaddle.getY();
            this.yPoints[2] = cpuPaddle.getY();
            this.yPoints[3] = cpuPaddle.getY() - this.HEIGHT;
            speedBoost++;
        }

        // bottom of CPU
        else if(this.yPoints[0] < cpuPaddle.getY() + cpuPaddle.getHEIGHT() &&
                this.yPoints[0] > cpuPaddle.getY() + cpuPaddle.getHEIGHT() - 10 &&
                this.xPoints[2] <= cpuPaddle.getX() + cpuPaddle.getWIDTH() &&
                this.xPoints[0] >= cpuPaddle.getX()) {
            this.velY = -this.velY;
            this.yPoints[0] = cpuPaddle.getY() + cpuPaddle.getHEIGHT() + this.HEIGHT;
            this.yPoints[1] = cpuPaddle.getY() + cpuPaddle.getHEIGHT();
            this.yPoints[2] = cpuPaddle.getY() + cpuPaddle.getHEIGHT();
            this.yPoints[3] = cpuPaddle.getY() + cpuPaddle.getHEIGHT() + this.HEIGHT;
            speedBoost++;
        }

        // middle of CPU
        if((this.xPoints[2] > cpuPaddle.getX()) &&
                (this.xPoints[2] < cpuPaddle.getX() + cpuPaddle.getWIDTH()) &&
                (this.yPoints[0] > (cpuPaddle.getLeftMidPoint().getY() - 10)) &&
                (this.yPoints[0] < (cpuPaddle.getLeftMidPoint().getY() + 10))) {
            //this.velX = -Math.round((float) (velX * Math.cos(0)));
            //this.velY = Math.round((float) (velY * Math.sin(0)));
            this.velX *= -1;
            this.xPoints[0] = cpuPaddle.getX() - this.WIDTH;
            this.xPoints[1] = cpuPaddle.getX() - this.WIDTH;
            this.xPoints[2] = cpuPaddle.getX();
            this.xPoints[3] = cpuPaddle.getX();
            speedBoost++;
        }

        // bottom half of CPU
        else if(this.xPoints[2] > cpuPaddle.getX() &&
                this.xPoints[2] < cpuPaddle.getX() + cpuPaddle.getWIDTH() &&
                this.yPoints[0] > (cpuPaddle.getLeftMidPoint().getY() + 10) &&
                this.yPoints[0] < (cpuPaddle.getY() + cpuPaddle.getHEIGHT())) {
            //this.velX = -Math.round((float)((velX + 1) * Math.cos((this.y - cpuPaddle.getLeftMidPoint().getY()) * (Math.PI / 180))));
            //this.velY = Math.round((float)((velY + 1) * Math.sin((this.y - cpuPaddle.getLeftMidPoint().getY()) * (Math.PI / 180))));
            this.velX *= -1;
            this.xPoints[0] = cpuPaddle.getX() - this.WIDTH;
            this.xPoints[1] = cpuPaddle.getX() - this.WIDTH;
            this.xPoints[2] = cpuPaddle.getX();
            this.xPoints[3] = cpuPaddle.getX();
            speedBoost++;
        }

        // top half of CPU
        else if(this.xPoints[2] > cpuPaddle.getX() &&
                this.xPoints[2] < cpuPaddle.getX() + cpuPaddle.getWIDTH() &&
                this.yPoints[0] < (cpuPaddle.getLeftMidPoint().getY() - 10) &&
                this.yPoints[1] > (cpuPaddle.getY())) {
            //this.velX = -Math.round((float)((velX + 1) * Math.cos(-(this.y - cpuPaddle.getLeftMidPoint().getY()) * (Math.PI / 180))));
            //this.velY = Math.round((float)((velY + 1) * Math.sin(-(this.y - cpuPaddle.getLeftMidPoint().getY()) * (Math.PI / 180))));
            this.velX *= -1;
            this.xPoints[0] = cpuPaddle.getX() - this.WIDTH;
            this.xPoints[1] = cpuPaddle.getX() - this.WIDTH;
            this.xPoints[2] = cpuPaddle.getX();
            this.xPoints[3] = cpuPaddle.getX();
            speedBoost++;
        }
    }

    /**
     * Tests if this ball hit a paddle that can rotate. Starts by calling a helper function intersects(). If some of
     * the ball and paddle overlap, then determines how to calculate a trajectory by figuring out which part of the
     * paddle the ball hit. Rotates the paddle by the amount of degrees rotated back to an "upright" position and
     * rotates the ball the same way. Calculates, then reverts the ball and paddle to their original orientations.
     * @param turnPaddle The Paddle in question.
     */
    private void reflectOffTurnPaddle(TurnPaddle turnPaddle) {
        if(this.intersects(turnPaddle)) {
            System.out.println("hit");

            // rotate the paddle and ball by the paddle's current angle of rotation so that they're "neutral"
            double radians = turnPaddle.getDegreesRotated();
            //double radians = Math.PI;
            //System.out.println(radians);
            double[] paddlexPoints = turnPaddle.getxPoints();
            double[] paddleyPoints = turnPaddle.getyPoints();
            double midpointX = turnPaddle.getMidpointX();
            double midpointY = turnPaddle.getMidpointY();
            //System.out.println(midpointX);
            //System.out.println(midpointY);

            // rotate the top left vertex
            double x1 = paddlexPoints[0] - midpointX;
            double y1 = paddleyPoints[0] - midpointY;

            double x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            double y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddlexPoints[0] = x2 + midpointX;
            paddleyPoints[0] = y2 + midpointY;

            // rotate the bottom left vertex
            x1 = paddlexPoints[1] - midpointX;
            y1 = paddleyPoints[1] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddlexPoints[1] = x2 + midpointX;
            paddleyPoints[1] = y2 + midpointY;

            // rotate the bottom right vertex
            x1 = paddlexPoints[2] - midpointX;
            y1 = paddleyPoints[2] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddlexPoints[2] = x2 + midpointX;
            paddleyPoints[2] = y2 + midpointY;

            // rotate the top right vertex
            x1 = paddlexPoints[3] - midpointX;
            y1 = paddleyPoints[3] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddlexPoints[3] = x2 + midpointX;
            paddleyPoints[3] = y2 + midpointY;


            // rotate the ball:
            // rotate the top left vertex
            x1 = this.xPoints[0] - midpointX;
            y1 = this.yPoints[0] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xPoints[0] = x2 + midpointX;
            this.yPoints[0] = y2 + midpointY;

            // rotate the bottom left vertex
            x1 = this.xPoints[1] - midpointX;
            y1 = this.yPoints[1] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xPoints[1] = x2 + midpointX;
            this.yPoints[1] = y2 + midpointY;

            // rotate the bottom right vertex
            x1 = this.xPoints[2] - midpointX;
            y1 = this.yPoints[2] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xPoints[2] = x2 + midpointX;
            this.yPoints[2] = y2 + midpointY;

            // rotate the top right vertex
            x1 = this.xPoints[3] - midpointX;
            y1 = this.yPoints[3] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xPoints[3] = x2 + midpointX;
            this.yPoints[3] = y2 + midpointY;


            // do the math:
            // "head" of the paddle:
            // case 1.1: bottom of the ball hits the head
            if(this.yPoints[1] > paddleyPoints[0] &&
                    this.yPoints[1] < paddleyPoints[0] + 10 &&
                    this.xPoints[1] <= paddlexPoints[2] &&
                    this.xPoints[2] >= paddlexPoints[0]) {
                this.velY = -this.velY;
                this.yPoints[0] = paddleyPoints[0] + this.HEIGHT;
                this.yPoints[1] = paddleyPoints[0];
                this.yPoints[2] = paddleyPoints[0];
                this.yPoints[3] = paddleyPoints[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("bottom to head");
            }
            // case 1.2: right side of the ball hits the head
            else if (this.yPoints[2] > paddleyPoints[0] &&
                    this.yPoints[2] < paddleyPoints[0] + 10 &&
                    this.xPoints[3] <= paddlexPoints[2] &&
                    this.xPoints[2] >= paddlexPoints[0]) {
                this.velY = -this.velY;
                this.velX = -this.velX;
                this.yPoints[2] = paddleyPoints[0];
                this.yPoints[3] = paddleyPoints[0];
                this.yPoints[0] = paddleyPoints[0] + this.HEIGHT;
                this.yPoints[1] = paddleyPoints[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("right to head");
            }
            // case 1.3: top of the ball hits the head
            else if (this.yPoints[0] > paddleyPoints[0] &&
                    this.yPoints[0] < paddleyPoints[0] + 10 &&
                    this.xPoints[0] <= paddlexPoints[2] &&
                    this.xPoints[3] >= paddlexPoints[0]) {
                this.velY = -this.velY;
                this.yPoints[0] = paddleyPoints[0];
                this.yPoints[3] = paddleyPoints[0];
                this.yPoints[1] = paddleyPoints[0] + this.HEIGHT;
                this.yPoints[2] = paddleyPoints[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("top to head");
            }
            // case 1.4: left side of the ball hits the head
            else if (this.yPoints[0] > paddleyPoints[0] &&
                    this.yPoints[0] < paddleyPoints[0] + 10 &&
                    this.xPoints[1] <= paddlexPoints[2] &&
                    this.xPoints[0] >= paddlexPoints[0]) {
                this.velY = -this.velY;
                this.velX = -this.velX;
                this.yPoints[0] = paddleyPoints[0];
                this.yPoints[1] = paddleyPoints[0];
                this.yPoints[2] = paddleyPoints[0] + this.HEIGHT;
                this.yPoints[3] = paddleyPoints[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("left to head");
            }

            // "tail" of the paddle
            // case 2.1: top of the ball hits the tail
            else if(this.yPoints[0] < paddleyPoints[0] + turnPaddle.getHEIGHT() &&
                    this.yPoints[0] > paddleyPoints[0] + turnPaddle.getHEIGHT() - 10 &&
                    this.xPoints[0] <= paddlexPoints[0] + turnPaddle.getWIDTH() &&
                    this.xPoints[2] >= paddlexPoints[0]) {
                this.velY = -this.velY;
                this.yPoints[0] = paddleyPoints[0] + turnPaddle.getHEIGHT();
                this.yPoints[1] = paddleyPoints[0] + turnPaddle.getHEIGHT() + this.HEIGHT;
                this.yPoints[2] = paddleyPoints[0] + turnPaddle.getHEIGHT() + this.HEIGHT;
                this.yPoints[3] = paddleyPoints[0] + turnPaddle.getHEIGHT();
                speedBoost++;
                System.out.println("top to tail");
            }
            // case 2.2: right side of the ball hits the tail
            else if (this.yPoints[2] > paddleyPoints[0] &&
                    this.yPoints[2] < paddleyPoints[0] + 10 &&
                    this.xPoints[2] <= paddlexPoints[2] &&
                    this.xPoints[3] >= paddlexPoints[0]) {
                this.velY = -this.velY;
                this.velX = -this.velX;
                this.yPoints[2] = paddleyPoints[0];
                this.yPoints[3] = paddleyPoints[0];
                this.yPoints[0] = paddleyPoints[0] + this.HEIGHT;
                this.yPoints[1] = paddleyPoints[0] + this.HEIGHT;
                speedBoost++;
                System.out.println("right to head");
            }
            // case 2.3: bottom of the ball hits the tail

            // case 2.4: left side of the ball hits the tail
            else if (this.yPoints[0] < paddleyPoints[1] &&
                    this.yPoints[0] > paddleyPoints[1] - 10 &&
                    this.xPoints[0] <= paddlexPoints[2] &&
                    this.xPoints[1] >= paddlexPoints[1]) {
                this.velY = -this.velY;
                this.velX = -this.velX;
                this.yPoints[0] = paddleyPoints[1];
                this.yPoints[1] = paddleyPoints[1];
                this.yPoints[2] = paddleyPoints[1] + this.HEIGHT;
                this.yPoints[3] = paddleyPoints[1] + this.HEIGHT;
                speedBoost++;
                System.out.println("left to head");
            }

            // case 3.1: top of ball hits left side of paddle
            else if (this.xPoints[0] > paddlexPoints[0] && this.xPoints[0] < (paddlexPoints[0] + turnPaddle.getWIDTH()) &&
                    this.yPoints[0] > paddleyPoints[0] && this.yPoints[0] < (paddleyPoints[0] + turnPaddle.getHEIGHT())) {
                this.velY = -this.velY;
                System.out.println("Top to left");
            }

            // case 3.2: right side of the ball hits left side of paddle
            // case 3.2.1: middle of paddle
            if((this.xPoints[0] + this.WIDTH < paddlexPoints[0] + turnPaddle.getWIDTH()) &&
                    (this.xPoints[0] + this.WIDTH > paddlexPoints[0]) &&
                    (this.yPoints[0] > (((paddleyPoints[0] + paddleyPoints[1]) / 2 ) - 10)) &&
                    (this.yPoints[0] < (((paddleyPoints[0] + paddleyPoints[1]) / 2 ) + 10))) {
                //this.velX = -Math.round((float) (velX * Math.cos(0)));
                //this.velY = Math.round((float) (velY * Math.sin(0)));
                this.velX *= -1;
                this.xPoints[0] = paddlexPoints[0] - this.WIDTH;
                this.xPoints[1] = paddlexPoints[0] - this.WIDTH;
                this.xPoints[2] = paddlexPoints[0];
                this.xPoints[3] = paddlexPoints[0];
                speedBoost++;
                System.out.println("Middle");
            }

            // case 3.2.2: bottom half of paddle
            else if(this.xPoints[0] + this.WIDTH > paddlexPoints[0] &&
                    this.xPoints[0] + this.WIDTH < paddlexPoints[0] + turnPaddle.getWIDTH() &&
                    this.yPoints[0] > (((paddleyPoints[0] + paddleyPoints[1]) / 2 ) + 10) &&
                    this.yPoints[0] < (paddleyPoints[0] + turnPaddle.getHEIGHT())) {
                //this.velX = -Math.round((float)((velX + 1) * Math.cos((this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
                //this.velY = Math.round((float)((velY + 1) * Math.sin((this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
                this.velX *= -1;
                this.xPoints[0] = paddlexPoints[0] - this.WIDTH;
                this.xPoints[1] = paddlexPoints[0] - this.WIDTH;
                this.xPoints[2] = paddlexPoints[0];
                this.xPoints[3] = paddlexPoints[0];
                speedBoost++;
                System.out.println("bottom");
            }

            // case 3.2.3: top half of paddle
            else if(this.xPoints[0] + this.WIDTH < paddlexPoints[0] + turnPaddle.getWIDTH() &&
                    this.xPoints[0] + this.WIDTH > paddlexPoints[0] &&
                    this.yPoints[0] < (((paddleyPoints[0] + paddleyPoints[1]) / 2 ) - 10) &&
                    this.yPoints[1] > (paddleyPoints[0])) {
                //this.velX = -Math.round((float)((velX + 1) * Math.cos(-(this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
                //this.velY = Math.round((float)((velY + 1) * Math.sin(-(this.y - paddle.getRightMidPoint().getY()) * (Math.PI / 180))));
                this.velX *= -1;
                this.xPoints[0] = paddlexPoints[0] - this.WIDTH;
                this.xPoints[1] = paddlexPoints[0] - this.WIDTH;
                this.xPoints[2] = paddlexPoints[0];
                this.xPoints[3] = paddlexPoints[0];
                speedBoost++;
                System.out.println("Top");
            }

            // case 3.3: bottom of the ball hits the left side of paddle
            else if (this.xPoints[1] > paddlexPoints[0] && this.xPoints[1] < (paddlexPoints[0] + turnPaddle.getWIDTH()) &&
                    this.yPoints[2] > paddleyPoints[0] && this.yPoints[1] < (paddleyPoints[0] + turnPaddle.getHEIGHT())) {
                this.velY = -this.velY;
                System.out.println("bottom to left");
            }

            // case 3.4: left side of the ball hits the left side of paddle
            else if (this.xPoints[0] > paddlexPoints[0] && this.xPoints[0] < (paddlexPoints[0] + turnPaddle.getWIDTH()) &&
                    this.yPoints[0] > paddleyPoints[0] && this.yPoints[0] < (paddleyPoints[0] + turnPaddle.getHEIGHT())) {
                this.velX = -this.velX;
                System.out.println("left to left");
            }

            // case 4.1: top of ball hits right side of paddle
            else if (this.xPoints[0] < paddlexPoints[0] + turnPaddle.getWIDTH() && this.xPoints[0] > paddlexPoints[0] &&
                    this.yPoints[3] > paddleyPoints[0] && this.yPoints[0] < paddleyPoints[0] + turnPaddle.getHEIGHT()) {
                this.velY = -this.velY;
                System.out.println("Top to right");
            }

            // case 4.2: right side of the ball hits right side of paddle
            else if (this.xPoints[2] < paddlexPoints[0] + turnPaddle.getWIDTH() && this.xPoints[2] > paddlexPoints[0] &&
                    this.yPoints[2] > paddleyPoints[0] && this.yPoints[2] < paddleyPoints[0] + turnPaddle.getHEIGHT()) {
                this.velX = -this.velX;
                System.out.println("Right to right");
            }

            // case 4.3: bottom of the ball hits the right side of paddle
            else if (this.xPoints[1] < paddlexPoints[0] + turnPaddle.getWIDTH() && this.xPoints[1] > paddlexPoints[0] &&
                    this.yPoints[1] > paddleyPoints[0] && this.yPoints[1] < paddleyPoints[0] + turnPaddle.getHEIGHT()) {
                this.velY = -this.velY;
                System.out.println("Bottom to right");
            }

            // case 4.4: left side of the ball hits the right side of paddle
            else if (this.xPoints[0] < paddlexPoints[0] + turnPaddle.getWIDTH() && this.xPoints[0] > paddlexPoints[0] &&
                    this.yPoints[0] > paddleyPoints[0] && this.yPoints[1] < paddleyPoints[0] + turnPaddle.getHEIGHT()) {
                this.velX = -this.velX;
                System.out.println("left to right");
            }

            // put the everything back to where it should be
            // negate the angle
            radians *= -1;
            // rotate the top left vertex
            x1 = paddlexPoints[0] - midpointX;
            y1 = paddleyPoints[0] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddlexPoints[0] = x2 + midpointX;
            paddleyPoints[0] = y2 + midpointY;

            // rotate the bottom left vertex
            x1 = paddlexPoints[1] - midpointX;
            y1 = paddleyPoints[1] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddlexPoints[1] = x2 + midpointX;
            paddleyPoints[1] = y2 + midpointY;

            // rotate the bottom right vertex
            x1 = paddlexPoints[2] - midpointX;
            y1 = paddleyPoints[2] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddlexPoints[2] = x2 + midpointX;
            paddleyPoints[2] = y2 + midpointY;

            // rotate the top right vertex
            x1 = paddlexPoints[3] - midpointX;
            y1 = paddleyPoints[3] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            paddlexPoints[3] = x2 + midpointX;
            paddleyPoints[3] = y2 + midpointY;


            // rotate the ball:
            // rotate the top left vertex
            x1 = this.xPoints[0] - midpointX;
            y1 = this.yPoints[0] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xPoints[0] = x2 + midpointX;
            this.yPoints[0] = y2 + midpointY;

            // rotate the bottom left vertex
            x1 = this.xPoints[1] - midpointX;
            y1 = this.yPoints[1] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xPoints[1] = x2 + midpointX;
            this.yPoints[1] = y2 + midpointY;

            // rotate the bottom right vertex
            x1 = this.xPoints[2] - midpointX;
            y1 = this.yPoints[2] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xPoints[2] = x2 + midpointX;
            this.yPoints[2] = y2 + midpointY;

            // rotate the top right vertex
            x1 = this.xPoints[3] - midpointX;
            y1 = this.yPoints[3] - midpointY;

            x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
            y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

            this.xPoints[3] = x2 + midpointX;
            this.yPoints[3] = y2 + midpointY;
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
        if(x >= this.xPoints[0] && x <= this.xPoints[0] + this.WIDTH && y >= this.yPoints[0] && y <= this.yPoints[0] + this.HEIGHT) {
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
        double xPoints[] = paddle.getxPoints();
        double yPoints[] = paddle.getyPoints();

        double x, y;

        // relative to the neutral upright position...
        // is the ball overlapping the top left corner?
        if(this.contains(xPoints[0], yPoints[0])) {
            return true;
        }

        // is the ball overlapping halfway between the left midpoint and the top left vertex?
        x = (((xPoints[0] + xPoints[1]) / 2) + xPoints[0]) / 2;
        y = (((yPoints[0] + yPoints[1]) / 2) + yPoints[0]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping the midpoint down the left side?
        x = (xPoints[0] + xPoints[1]) / 2;
        y = (yPoints[0] + yPoints[1]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping halfway between the left midpoint and the bottom left vertex?
        x = (((xPoints[0] + xPoints[1]) / 2) + xPoints[1]) / 2;
        y = (((yPoints[0] + yPoints[1]) / 2) + yPoints[1]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping the bottom left vertex?
        if(this.contains(xPoints[1], yPoints[1])) {
            return true;
        }

        // is the ball overlapping the bottom right vertex?
        if(this.contains(xPoints[2], yPoints[2])) {
            return true;
        }

        // is the ball overlapping halfway between the right side midpoint and the bottom right vertex?
        x = (((xPoints[2] + xPoints[3]) / 2) + xPoints[2]) / 2;
        y = (((xPoints[2] + xPoints[3]) / 2) + xPoints[2]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping the midpoint on the right side?
        x = (xPoints[2] + xPoints[3]) / 2;
        y = (yPoints[2] + yPoints[3]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping halfway between the right side midpoint and the top right vertex?
        x = (((xPoints[2] + xPoints[3]) / 2) + xPoints[3]) / 2;
        y = (((xPoints[2] + xPoints[3]) / 2) + xPoints[3]) / 2;
        if(this.contains(x, y)) {
            return true;
        }

        // is the ball overlapping the top right vertex?
        if(this.contains(xPoints[3], yPoints[3])) {
            return true;
        }

        return false;
    }

    public double[] getxPoints() {
        return xPoints;
    }

    public double[] getyPoints() {
        return yPoints;
    }

    public void setScored(boolean b) {
        scored = b;
    }

    /**
     * To be called by Enemy_H
     */
    protected void set_x_points(double [] x_points) {
        this.xPoints = x_points;
    }

    /**
     * To be called by Enemy_H
     */
    protected void set_y_points(double[] y_points) {
        this.yPoints = y_points;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }
}
