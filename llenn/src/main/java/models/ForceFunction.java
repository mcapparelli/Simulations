package models;

import java.util.List;

public interface ForceFunction {
    public Point getForce(Point position, Point velocity, List<Particle> neighbors);
}
