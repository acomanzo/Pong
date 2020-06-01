package com.main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 1000, HEIGHT = WIDTH / 12 * 9;

    private Thread thread;

    private boolean running = false;

    private Handler crazyModeHandler;
    private Handler classicModeHandler;

    private SpawnerManager spawnerManager;

    private Menu menu;

    private Timer crazyModeTimer;
    private Timer classicModeTimer;

    private HUD HUD;

    private ClassicPaddle player1;
    private TurnPaddle turnPaddle;
    private ClassicPaddle classicModePlayer;

    private Ball crazyModeBall;
    private boolean pauseCrazyModeBall = false;
    private Ball classicModeBall;

    private CPUPaddle cpuPaddle;
    private CPUPaddle classicModeCpu;

    private Enemy_H enemy_h;
    private Enemy_V enemy_v;

    private int playerScore = 0;
    private int cpuScore = 0;

    protected boolean crazyModeCountdownStarted = false;
    protected boolean classicModeCountdownStarted = false;

    protected boolean toggleHitBoxes = false;

    private String state = "MAIN MENU";

    private CollisionManager crazyModeCollisionManager;
    private CollisionManager classicModeCollisionManager;

    /**
     * Initializes several variables crucial to the game and adds game objects to the handler.
     */
    public Game() {
        makeCrazyMode();
        makeClassicMode();

        this.addKeyListener(new KeyInput(player1, classicModePlayer, this, turnPaddle));

        new Window(WIDTH, HEIGHT, this);

        menu = new Menu (this);
        this.addMouseListener(menu);
    }

    private void makeCrazyMode() {
        Color myRed = new Color(247, 23, 53);
        Color darkBlue = new Color (35, 22, 81);
        Color myTeal = new Color (65, 234, 212);
        Color myWhite = new Color (253, 255, 252);
        Color salmon = new Color (255, 132, 132);

        crazyModeHandler = new Handler();

        crazyModeCollisionManager = new CollisionManager();

        player1 = new ClassicPaddle(50, Math.round(HEIGHT / 2 - 50), 20, 100, this, myTeal, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)));
        turnPaddle = new TurnPaddle(750, 200, 20, 100, this, salmon, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)));
        cpuPaddle = new CPUPaddle(930, HEIGHT / 2 - 50, 20, 100, this, darkBlue, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)));

        crazyModeTimer = new Timer(this, "CRAZY_MODE", darkBlue);

        ArrayList<SimpleGameObject> gameObjects = new ArrayList<>();
        gameObjects.add(player1);
        gameObjects.add(turnPaddle);
        gameObjects.add(cpuPaddle);
        crazyModeBall = new Ball(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20, crazyModeTimer, crazyModeHandler, this, myRed, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)));

        HUD = new HUD(5, 5, this, crazyModeTimer, crazyModeBall, darkBlue);
        this.addMouseListener(HUD);

        spawnerManager = new SpawnerManager(crazyModeHandler, HUD, crazyModeTimer, this, null);

        enemy_h = new Enemy_H(WIDTH / 2, 100, 2, 0, 100, 50, Color.BLACK, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)), spawnerManager);
        enemy_v = new Enemy_V(600, 200, 0, 2, 50, 100, Color.BLACK, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)), spawnerManager);

        spawnerManager.setBall(crazyModeBall);
        spawnerManager.setEnemy_h(enemy_h);
        spawnerManager.setEnemy_v(enemy_v);

        crazyModeHandler.addObject(player1.getId(), player1);
        crazyModeHandler.addObject(turnPaddle.getId(), turnPaddle);
        crazyModeHandler.addObject(crazyModeBall.getId(), crazyModeBall);
        crazyModeHandler.addObject(cpuPaddle.getId(), cpuPaddle);
        crazyModeHandler.addObject(enemy_h.getId(), enemy_h);

        crazyModeCollisionManager.add(crazyModeBall);
        crazyModeCollisionManager.add(player1);
        crazyModeCollisionManager.add(turnPaddle);
        crazyModeCollisionManager.add(cpuPaddle);
        crazyModeCollisionManager.add(enemy_h);
        crazyModeCollisionManager.add(enemy_v);

        spawnerManager.setCollisionManager(crazyModeCollisionManager);
    }

    private void makeClassicMode() {
        Color tan = new Color(255, 241, 208);
        Color myRed = new Color(221, 29, 26);
        Color myGold = new Color (240, 200, 8);
        Color myBlue = new Color (7, 160, 195);
        Color myDarkBlue = new Color (8, 103, 136);

        classicModeHandler = new Handler();

        classicModeCollisionManager = new CollisionManager();

        classicModeCpu = new CPUPaddle(930, HEIGHT / 2 - 50, 20, 100, this, myBlue, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)));
        classicModePlayer = new ClassicPaddle(50, Math.round(HEIGHT / 2 - 50), 20, 100, this, myBlue, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)));
        classicModeTimer = new Timer(this, "CLASSIC_MODE", myDarkBlue);
        ArrayList<SimpleGameObject> gameObjects = new ArrayList<>();
        gameObjects.add(classicModeCpu);
        gameObjects.add(classicModePlayer);
        classicModeBall = new Ball(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20, classicModeTimer, classicModeHandler, this, myGold, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)));

        HUD = new HUD(5, 5, this, crazyModeTimer, crazyModeBall, myRed);
        this.addMouseListener(HUD);

        classicModeHandler.addObject(classicModePlayer.getId(), classicModePlayer);
        classicModeHandler.addObject(classicModeBall.getId(), classicModeBall);
        classicModeHandler.addObject(classicModeCpu.getId(), classicModeCpu);

        classicModeCollisionManager.add(classicModeBall);
        classicModeCollisionManager.add(classicModePlayer);
        classicModeCollisionManager.add(classicModeCpu);
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

        if (state.equals("CRAZY_MODE")) {
            crazyModeHandler.tick();
            crazyModeTimer.tick(crazyModeCountdownStarted);
            crazyModeCountdownStarted = true;
            spawnerManager.tick();
            HUD.tick(playerScore, cpuScore);
            crazyModeCollisionManager.tick();
        }
        else if (state.equals("CLASSIC_MODE")) {
            classicModeHandler.tick();
            classicModeTimer.tick(classicModeCountdownStarted);
            classicModeCountdownStarted = true;
            crazyModeTimer.tick(crazyModeCountdownStarted);
            HUD.tick(playerScore, cpuScore);
            classicModeCollisionManager.tick();
        }
        else if (state.equals("MAIN MENU")) {
            menu.tick();
            crazyModeTimer.tick(crazyModeCountdownStarted);
            classicModeTimer.tick(classicModeCountdownStarted);
        }
        else {
            System.out.println("Error in tick, Game.java");
            System.exit(1);
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

        if (state.equals("CRAZY_MODE")) {
            Color myRed = new Color(247, 23, 53);
            Color darkBlue = new Color (35, 22, 81);
            Color myTeal = new Color (65, 234, 212);
            Color myWhite = new Color (253, 255, 252);
            Color salmon = new Color (255, 132, 132);

            g.setColor(myWhite);
            g.fillRect(0,0, WIDTH, HEIGHT);

            g.setColor(myRed);
            g.drawOval(WIDTH / 2 - 50, HEIGHT / 2 - 50, 100, 100);
            g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

            crazyModeHandler.render(g);
            crazyModeTimer.render(g);
            HUD.render(g);
        }
        else if (state.equals("CLASSIC_MODE")) {
            Color tan = new Color(255, 241, 208);
            Color myRed = new Color(221, 29, 26);
            Color myGold = new Color (240, 200, 8);
            Color myBlue = new Color (7, 160, 195);
            Color myDarkBlue = new Color (8, 103, 136);

            g.setColor(tan);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(myDarkBlue);
            g.drawOval(WIDTH / 2 - 50, HEIGHT / 2 - 50, 100, 100);
            g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

            classicModeHandler.render(g);
            classicModeTimer.render(g);
            HUD.render(g);
        }
        else {
            //System.out.println("Error in render, Game.java");
            //System.exit(1);
        }

        g.dispose();
        bs.show();
    }

    public void setPlayerScore(int score) {
        playerScore = score;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setCpuScore (int score) {
        cpuScore = score;
    }

    public int getCpuScore () {
        return cpuScore;
    }

    public Ball getBall(String state) {

        if (state.equals("CRAZY_MODE")) {
            return crazyModeBall;
        }
        else if (state.equals("CLASSIC_MODE")) {
            return classicModeBall;
        }
        else {
            System.out.println("Error in getBall, Game.java");
            System.exit(1);
            return null;
        }
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

    public boolean getPauseCrazyModeBall() {
        return pauseCrazyModeBall;
    }

    public void setPauseCrazyModeBall(boolean pauseCrazyModeBall) {
        this.pauseCrazyModeBall = pauseCrazyModeBall;
    }

}
