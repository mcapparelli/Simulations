package models;

import java.util.List;

public interface ForceFunction {
    public Vector2D getForce(Vector2D position, Vector2D velocity, List<Particle> neighbors);
}
