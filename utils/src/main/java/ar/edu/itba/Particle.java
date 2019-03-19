package ar.edu.itba;

public class Particle {
    private static int quantity = 0;
    private int id;
    private double ratio;
    private Property<String> property;
    private Point location;
    private double velocity;
    private double angle;

    public Particle(double ratio, Property<String> property, Point location, double velocity, double angle) {
        quantity++;
        this.id = quantity;
        this.ratio = ratio;
        this.property = property;
        this.location = location;
        this.velocity = velocity;
        this.angle = angle;
    }

    public Particle(int id, double ratio, Property<String> property, Point location, double velocity, double angle) {
        this.id = id;
        this.ratio = ratio;
        this.property = property;
        this.location = location;
        this.velocity = velocity;
        this.angle = angle;
    }

    public int getId() {
        return id;
    }

    public double getRatio() {
        return ratio;
    }

    public Point getLocation() {
        return location;
    }

    public static double distanceBetweenMolecules(Particle m1, Particle m2){
        return Point.distanceBetween(m1.location,m2.location);
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", ratio=" + ratio +
                ", property=" + property +
                ", location=" + location +
                ", velocity=" + velocity +
                '}';
    }
}
