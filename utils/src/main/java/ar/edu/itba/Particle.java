package ar.edu.itba;

public class Particle {
    private static int quantity = 0;

    private int id;

    private double radio;
    private Property<String> property;
    private Point location;
    private double velocity;
    private double angle;

    public Particle(double radio, Property<String> property, Point location, double velocity, double angle) {
        quantity++;
        this.id = quantity;
        this.radio = radio;
        this.property = property;
        this.location = location;
        this.velocity = velocity;
        this.angle = angle;
    }

    public Particle(double radio, Point location, double velocity, double angle) {
        quantity++;
        this.id = quantity;
        this.radio = radio;
        this.property = null;
        this.location = location;
        this.velocity = velocity;
        this.angle = angle;
    }

    public Particle(int id, double radio, Point location, double velocity, double angle) {
        this.id = id;
        this.radio = radio;
        this.property = null;
        this.location = location;
        this.velocity = velocity;
        this.angle = angle;
    }

    public Particle(int id, double radio, Property<String> property, Point location, double velocity, double angle) {
        this.id = id;
        this.radio = radio;
        this.property = property;
        this.location = location;
        this.velocity = velocity;
        this.angle = angle;
    }

    public int getId() {
        return id;
    }

    public double getRadio() {
        return radio;
    }

    public Point getLocation() {
        return location;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public static double distanceBetweenParticles(Particle m1, Particle m2){
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
                ", radio=" + radio +
                ", property=" + property +
                ", location=" + location +
                ", velocity=" + velocity +
                '}';
    }
}
