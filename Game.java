package com.main;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 1000, HEIGHT = WIDTH / 12 * 9;

    private Thread thread;

    private boolean running = false;

    private Handler handler;

    private Spawner spawner;

    private Menu menu;

    private Timer timerObj;
    private HUD HUD;

    private Paddle player1;
    private Paddle player2;

    private TurnPaddle turnPaddle;

    private Ball ball;
    private CircleBall circleBall;

    private CPUPaddle cpuPaddle;

    private Enemy_H enemy_h;
    private Enemy_V enemy_v;

    private int player1Score = 0;
    private int player2Score = 0;

    private long elapsedSeconds = 0;

    boolean countdownStarted = false;

    boolean toggleHitboxes = false;

    private String state = "MAIN MENU";

    /**
     * Initializes several variables crucial to the game and adds game objects to the handler.
     */
    public Game() {
        handler = new Handler();

        player1 = new Paddle(50, Math.round(HEIGHT / 2 - 50), this);
        //player2 = new Paddle(250, Math.round(HEIGHT / 2 - 50), this);

        turnPaddle = new TurnPaddle(750, 200, this);

        this.addKeyListener(new KeyInput(handler, player1, player2, this, turnPaddle));

        new Window(WIDTH, HEIGHT, this);

        menu = new Menu (this, handler);

        this.addMouseListener(menu);

        timerObj = new Timer(this, player1, player2);

        //cpuPaddle = new CPUPaddle(930, HEIGHT / 2 - 50, this, ball);

        cpuPaddle = new CPUPaddle(930, HEIGHT / 2 - 50, this);

        ball = new Ball(WIDTH / 2 - 10, HEIGHT / 2 - 10, player1, turnPaddle, timerObj, handler, this, cpuPaddle);
        circleBall = new CircleBall(WIDTH / 2 -10, HEIGHT / 2 - 10, player1, turnPaddle, timerObj, handler, this, cpuPaddle);

        HUD = new HUD(5, 5, this, timerObj, ball);
        this.addMouseListener(HUD);

        //player1 = new Paddle()

        enemy_h = new Enemy_H(600, 100, ball);
        enemy_v = new Enemy_V(500, 100, ball);

        spawner = new Spawner(handler, HUD, timerObj, enemy_h, enemy_v, ball);

        handler.addObject(player1);
        //handler.addObject(player2);
        handler.addObject(turnPaddle);
        handler.addObject(ball);
        handler.addObject(cpuPaddle);
        //handler.addObject(circleBall);
        handler.addObject(enemy_h);
        //handler.addObject(spawner);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                delta--;
            }
            if(running) {
                render();
            }
            frames++;
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {

        if (state.equals("PLAY")) {
            handler.tick();
            spawner.tick();
            timerObj.tick(countdownStarted);
            countdownStarted = true;
            elapsedSeconds = timerObj.getElapsedSeconds();
            HUD.tick(player1Score, player2Score);
        }

        else {
            menu.tick();
            timerObj.tick(countdownStarted);
        }

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        if (state.equals("MAIN MENU")) {
            menu.render(g);
        }

        if (state.equals("PLAY")) {
            g.setColor(Color.GRAY);
            g.fillRect(0,0, WIDTH, HEIGHT);

            g.setColor(Color.BLACK);
            g.drawOval(WIDTH / 2 - 50, HEIGHT / 2 - 50, 100, 100);
            g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

            handler.render(g);
            timerObj.render(g);
            HUD.render(g);
        }

        g.dispose();
        bs.show();
    }

    public void setPlayer1Score(int score) {
        this.player1Score = score;
    }

    public void setPlayer2Score(int score) {
        this.player2Score = score;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public Ball getBall() {
        return ball;
    }

    public static void main(String[] args) {
        new Game();
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState () {
        return state;
    }
}
