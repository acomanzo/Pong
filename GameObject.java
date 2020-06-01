package com.main;
import java.awt.*;

public interface GameObject {

    public abstract void tick();

    public abstract void render(Graphics g);
}
