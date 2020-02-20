package com.main;

/**
 * A class that handles which enemies to tick/render
 */
public class Spawner {

    private Handler handler;
    private HUD hud;
    private Timer timer;

    private Enemy_H enemy_h;
    private Enemy_V enemy_v;

    private Ball ball;

    public Spawner (Handler handler, HUD hud, Timer timer, Enemy_H enemy_h, Enemy_V enemy_v, Ball ball) {
        this.handler = handler;
        this.hud = hud;
        this.timer = timer;
        this.enemy_h = enemy_h;
        this.enemy_v = enemy_v;
        this.ball = ball;
    }

    public void tick() {
        if (enemy_h.isDead()) {
            enemy_h.setHealth(100);
            enemy_h.resurrect();
            handler.removeObject(enemy_h);
            handler.addObject(enemy_v);
            System.out.println("Horizontal enemy dead, adding vertical enemy.");
        }

        if (enemy_v.isDead()) {
            enemy_v.setHealth(100);
            enemy_v.resurrect();
            handler.removeObject(enemy_v);
            handler.addObject(enemy_h);
            System.out.println("Vertical enemy dead, adding horizontal enemy.");
        }
    }
}
