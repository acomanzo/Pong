package com.main;

import java.util.ArrayList;

/**
 * Checks every object in the game and checks if any collide by the separated axis theorem.
 */
public class CollisionManager {

    // a list of game objects that can collide
    private ArrayList<SimpleGameObject> objects;

    public CollisionManager() {
        this(new ArrayList<>());
    }

    public CollisionManager(ArrayList<SimpleGameObject> objects) {
        this.objects = objects;
    }

    public SimpleGameObject tick() {
        for (int i = 0; i < objects.size(); i++) {
            for (int j = 0; j < objects.size(); j++) {
                if ((objects.get(i) instanceof Enemy_H && objects.get(j) instanceof Enemy_V) || (objects.get(i) instanceof Enemy_V && objects.get(j) instanceof Enemy_H))
                    continue;
                /*if ((objects.get(i) instanceof Enemy_H && objects.get(j) instanceof EnemyBall) || (objects.get(i) instanceof EnemyBall && objects.get(j) instanceof Enemy_H))
                    continue;
                if ((objects.get(i) instanceof Enemy_V && objects.get(j) instanceof EnemyBall) || (objects.get(i) instanceof EnemyBall && objects.get(j) instanceof Enemy_V))
                    continue;*/
                if (!objects.get(i).equals(objects.get(j))) {
                    shapeOverlap_SAT_Static(objects.get(i), objects.get(j));
                }
            }
        }

        return null;
    }

    public boolean shapeOverlap_SAT(SimpleGameObject object1, SimpleGameObject object2) {

        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                SimpleGameObject temp = object1;
                object1 = object2;
                object2 = temp;
            }

            for (int i = 0; i < object1.getVertices().length; i++) {
                double[] axisProjection =
                        {-(object1.getVertices()[i + 1 == object1.getVertices().length ? 0 : i + 1].getY() - object1.getVertices()[i].getY()),
                                (object1.getVertices()[i + 1 == object1.getVertices().length ? 0 : i + 1].getX() - object1.getVertices()[i].getX())};

                double minObject1 = Integer.MAX_VALUE;
                double maxObject1 = Integer.MIN_VALUE;
                for (int j = 0; j < object1.getVertices().length; j++) {
                    double dotProduct = (object1.getVertices()[j].getX() * axisProjection[0] + object1.getVertices()[j].getY() * axisProjection[1]);
                    minObject1 = Math.min(minObject1, dotProduct);
                    maxObject1 = Math.max(maxObject1, dotProduct);
                }

                double minObject2 = Integer.MAX_VALUE;
                double maxObject2 = Integer.MIN_VALUE;
                for (int j = 0; j < object2.getVertices().length; j++) {
                    double dotProduct = (object2.getVertices()[j].getX() * axisProjection[0] + object2.getVertices()[j].getY() * axisProjection[1]);
                    minObject2 = Math.min(minObject2, dotProduct);
                    maxObject2 = Math.max(maxObject2, dotProduct);
                }

                if (!(maxObject2 >= minObject1 && maxObject1 >= minObject2)) {
                    object1.overlap = false;
                    return false;
                }
            }

        }
        object1.overlap = true;
        return true;
    }

    /**
     * Uses the separated axis theorem to find if the two objects are intersecting. The theorem says that if you can draw
     * a line between any two convex polygons and separates them completely, then the two polygons do not overlap.
     * The algorithm can exit once it determines the objects do not overlap on a single axis. The axes you are testing
     * are the normals to each shape's edges which the function finds by getting the line perpendicular to the edge.
     * Then for each shape, each edge is projected onto the normal, and we store the minimum and maximum projection
     * out of each edge.
     * @param object1 the first convex polygon
     * @param object2 the second convex polygon
     * @return false if the polygons are not colliding, true otherwise.
     */
    public boolean shapeOverlap_SAT_Static(SimpleGameObject object1, SimpleGameObject object2) {

        double overlap = Integer.MAX_VALUE;

        // do this process for each each
        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                SimpleGameObject temp = object1;
                object1 = object2;
                object2 = temp;
            }

            // repeat for all of object1's edges
            for (int i = 0; i < object1.getVertices().length; i++) {
                // get the normal vector of object1's edge
                double[] axisProjection =
                        {-(object1.getVertices()[i + 1 == object1.getVertices().length ? 0 : i + 1].getY() - object1.getVertices()[i].getY()),
                                (object1.getVertices()[i + 1 == object1.getVertices().length ? 0 : i + 1].getX() - object1.getVertices()[i].getX())};

                // project object1 onto the normal
                double minObject1 = Integer.MAX_VALUE;
                double maxObject1 = Integer.MIN_VALUE;
                for (int j = 0; j < object1.getVertices().length; j++) {
                    double dotProduct = (object1.getVertices()[j].getX() * axisProjection[0] + object1.getVertices()[j].getY() * axisProjection[1]);
                    minObject1 = Math.min(minObject1, dotProduct);
                    maxObject1 = Math.max(maxObject1, dotProduct);
                }

                // project object2 onto the normal
                double minObject2 = Integer.MAX_VALUE;
                double maxObject2 = Integer.MIN_VALUE;
                for (int j = 0; j < object2.getVertices().length; j++) {
                    double dotProduct = (object2.getVertices()[j].getX() * axisProjection[0] + object2.getVertices()[j].getY() * axisProjection[1]);
                    minObject2 = Math.min(minObject2, dotProduct);
                    maxObject2 = Math.max(maxObject2, dotProduct);
                }

                // Get the actual overlap between the edges and projected axis and store the minimum
                overlap = Math.min(Math.min(maxObject1, maxObject2) - Math.max(minObject1, minObject2), overlap);

                // if the shapes are not overlapping on this axis, exit
                if (!(maxObject2 >= minObject1 && maxObject1 >= minObject2)) {
                    return false;
                }

            }
        }

        if (object2 instanceof Ball)
            resolveCollisionBall_SAT(object2, object1, overlap);
        else if (object1 instanceof Ball)
            resolveCollisionBall_SAT(object1, object2, overlap);

        if (object2 instanceof Enemy_H)
            ((Enemy_H) object2).setHit(true);
        else if (object1 instanceof Enemy_H)
            ((Enemy_H) object1).setHit(true);

        if (object2 instanceof Enemy_V)
            ((Enemy_V) object2).setHit(true);
        else if (object1 instanceof Enemy_V)
            ((Enemy_V) object1).setHit(true);

        if (object2 instanceof EnemyBall)
            resolveCollisionBall_SAT(object2, object1, overlap);
        else if (object1 instanceof EnemyBall)
            resolveCollisionBall_SAT(object1, object2, overlap);


        return true;
    }

    /**
     * Resolves a collision between a ball and any other SimpleGameObject assuming the separated axis theorem. Finds
     * the reflection vector for the ball and displaces the ball so that it's not intersecting with the paddle.
     * @param ball the ball
     * @param other any other SimpleGameObject
     * @param overlap the amount that the two objects overlap
     */
    private void resolveCollisionBall_SAT(SimpleGameObject ball, SimpleGameObject other, double overlap) {
        // formula: r = d - 2(d dot n)n, r is the reflection vector, d is vector of incidence, n is normal
        double [] normal = new double[2];
        double [] reflection = new double[2];
        double [] incidence = {ball.velX, ball.velY};
        if (Math.sqrt(Math.pow(ball.getVertices()[0].getX(), 2) + Math.pow(other.getVertices()[0].getX(), 2)) <
                Math.sqrt(Math.pow(ball.getVertices()[2].getX(), 2) + Math.pow(other.getVertices()[2].getX(), 2))) {
            normal[0] = -(other.getVertices()[1].getY() - other.getVertices()[0].getY());
            normal[1] = (other.getVertices()[1].getX() - other.getVertices()[0].getX());
        }
        else {
            normal[0] = -(other.getVertices()[3].getY() - other.getVertices()[2].getY());
            normal[1] = (other.getVertices()[3].getX() - other.getVertices()[2].getX());
        }

        // normalize the normal
        double normalMagnitude = Math.sqrt(Math.pow(normal[0], 2) + Math.pow(normal[1], 2));
        normal[0] = normal[0] / normalMagnitude;
        normal[1] = normal[1] / normalMagnitude;

        // plug values into equation
        normal[0] = normal[0] * 2 * (incidence[0] * normal[0] + incidence[1]* normal[1]);
        normal[1] = normal[1] * 2 * (incidence[0] * normal[0] + incidence[1] * normal[1]);
        reflection[0] = (incidence[0] + -(normal[0])) % 10; // use mod 10 so that the vectors aren't too big
        reflection[1] = (incidence[1] + -(normal[1])) % 10;

        // set ball's new vector
        ball.setVelX(reflection[0]);
        ball.setVelY(reflection[1]);

        // displace the ball so it's not colliding with the paddle
        double[] displacement = {other.getX() - ball.getX(), other.getY() - ball.getY()};
        double displacementMag = Math.sqrt(Math.pow(displacement[0], 2) + Math.pow(displacement[1], 2));
        ball.setX(ball.getX() - (overlap / 8) * displacement[0] / displacementMag);
        ball.setY(ball.getY() - (overlap / 8) * displacement[1] / displacementMag);


        //((Ball) ball).incrementSpeedboost();

    }


    public void add (SimpleGameObject gameObject) {
        objects.add(gameObject);
    }

}
