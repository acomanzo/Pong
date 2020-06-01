package com.main;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class that handles which enemies to tick/render
 */
public class SpawnerManager {

    private Handler handler;
    private HUD hud;
    private Timer timer;

    private Enemy_H enemy_h;
    private Enemy_V enemy_v;
    private Ball ball;

    private Game game;

    private CollisionManager collisionManager;

    public SpawnerManager(Handler handler, HUD hud, Timer timer, Game game, CollisionManager collisionManager) {
        this.handler = handler;
        this.hud = hud;
        this.timer = timer;
        this.game = game;
        this.collisionManager = collisionManager;
    }

    public void tick() {
        if (enemy_h.isDead()) {
            enemy_h.setHealth(100);
            enemy_h.resurrect();
            handler.removeObject(enemy_h.getId());
            handler.addObject(enemy_v.getId(), enemy_v);
            System.out.println("Horizontal enemy dead, adding vertical enemy.");
        }

        if (enemy_v.isDead()) {
            enemy_v.setHealth(100);
            enemy_v.resurrect();
            handler.removeObject(enemy_v.getId());
            handler.addObject(enemy_h.getId(), enemy_h);
            System.out.println("Vertical enemy dead, adding horizontal enemy.");
        }
    }

    public void spawnBall(double x, double y) {
        if (collisionManager != null) {
            EnemyBall temp = new EnemyBall(x, y, 20, 20, Color.BLACK, String.valueOf(System.currentTimeMillis() * ThreadLocalRandom.current().nextInt(1, 1000 + 1)));
            handler.addObject(temp.getId(), temp);
            collisionManager.add(temp);
        }

    }

    public void setEnemy_h(Enemy_H enemy_h) {
        this.enemy_h = enemy_h;
    }

    public void setEnemy_v(Enemy_V enemy_v) {
        this.enemy_v = enemy_v;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void setCollisionManager(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

}
