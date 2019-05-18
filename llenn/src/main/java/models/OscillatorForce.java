package models;

import java.util.List;

public class OscillatorForce implements ForceFunction {
    private Double y;
    private Double k;

    public OscillatorForce(Double y, Double k) {
        this.k = k;
        this.y = y;
    }

    @Override
    public Point getForce(Point position, Point velocity, List<Particle> neighbors) {
        return new Point(position).multiply(-k).add(velocity.multiply(-y));
    }
}
