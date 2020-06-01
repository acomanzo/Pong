package com.main;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * A class that handles the tick and render methods of all game objects.
 */
public class Handler {

    LinkedHashMap<String, GameObject> objects = new LinkedHashMap<String, GameObject>();

    public void tick() {

        for (Map.Entry<String, GameObject> entry : objects.entrySet()) {
            entry.getValue().tick();
        }

        /*try {
            for (Map.Entry<String, GameObject> entry : objects.entrySet()) {
                entry.getValue().tick();
            }
        } catch (ConcurrentModificationException e) {
            System.out.println(e.getMessage());
        }*/

        /*for(int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);
            tempObject.tick();
        }*/
    }

    public void render(Graphics g) {
        for (Map.Entry<String, GameObject> entry : objects.entrySet()) {
            entry.getValue().render(g);
        }

        /*for(int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            tempObject.render(g);
        }*/
    }

    public void addObject(String key, GameObject value) {
        objects.put(key, value);
        // this.object.add(object);
    }

    public void removeObject(String key) {
        objects.remove(key);
        // this.object.remove(object);
    }

}
