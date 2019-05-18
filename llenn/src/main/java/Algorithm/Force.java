package Algorithm;

import javafx.geometry.Point2D;
import models.ForceFunction;
import models.Particle;
import models.Point;

import java.util.List;

public class Force implements ForceFunction {
    private Double rm;
    private Double e;

    public Force(Double rm, Double e) {
        this.rm = rm;
        this.e = e;
    }

    @Override
    public Point getForce(Point position, Point velocity, List<Particle> neighbours){
        Double xForce = 0.0, yForce = 0.0;
        Point2D pPoint = new Point2D(position.getX(),position.getY());
        for (Particle neighbour : neighbours) {
            Point2D nPoint = neighbour.point2D();
            Point2D distance = pPoint.subtract(nPoint);
            double forceMagnitude = calculateMagnitude(distance.magnitude());
            xForce += forceMagnitude * (distance.getX()) / distance.magnitude();
            yForce += forceMagnitude * (distance.getY()) / distance.magnitude();
        }
        return new Point(xForce, yForce);
    }

    private double calculateMagnitude(final double r) {
        return ( (12 * e) / rm) * (Math.pow(rm / r, 13) - Math.pow(rm / r, 7)) ;
    }
}
