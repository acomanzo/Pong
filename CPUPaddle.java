package com.main;

import java.awt.*;

/**
 * Represents a computer player.
 */
public class CPUPaddle extends SimpleGameObject {

    private Game game;

    public CPUPaddle(int x, int y, int width, int height, Game game, Color color, String id) {
        super(x, y, 0, 5, height, width, color, id);

        this.game = game;

    }

    public void tick() {

        if(midPoint.y < (game.getBall(game.getState()).midPoint.y)) {
            setY(midPoint.y + velY);
        }

        if(midPoint.y > (game.getBall(game.getState()).midPoint.y)) {
            setY(midPoint.y - velY);
        }

        if(vertices[0].y <= 0)
            setY(HEIGHT / 2);
        if(vertices[0].y > game.HEIGHT - HEIGHT - 23)
            setY(game.HEIGHT - (HEIGHT / 2) - 23);

    }

    public void render(Graphics g) {
        g.setColor(color);
        g.fillPolygon(new int[] {(int) vertices[0].x, (int) vertices[1].x, (int) vertices[2].x, (int) vertices[3].x},
                new int[] {(int) vertices[0].y, (int) vertices[1].y, (int) vertices[2].y, (int) vertices[3].y}, 4);

    }

}
