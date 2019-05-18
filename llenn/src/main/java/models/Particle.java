package models;

import java.util.Optional;

public class Particle {
    private static Long id_class = Long.valueOf(0);

    private final double radius;
    private final double mass;
    private State previousState;
    private State currentState;
    private State futureState;

    public Particle(double radius, double mass) {
        this.id_class = id_class++;
        this.radius = radius;
        this.mass = mass;
        this.previousState = new State();
        this.currentState = new State();
        this.futureState = null;
    }

    public Particle(double radius, double mass, State previousState, State currentState) {
        this.id_class = id_class++;
        this.radius = radius;
        this.mass = mass;
        this.previousState = previousState;
        this.currentState = currentState;
        this.futureState = null;
    }

    public Particle(double radius, double mass, State currentState) {
        this.id_class = id_class++;
        this.radius = radius;
        this.mass = mass;
        this.previousState =  new State(0, 0, 0, 0, 0, 0);
        this.currentState = currentState;
        this.futureState = null;
    }

    /**
     * Big particles without radio and with infinite mass for simulating walls.
     */
    public Particle(long id,double x, double y) {
        this.id_class = id;
        currentState = new State(x, y, 0, 0, 0, 0);
        previousState = new State(0, 0, 0, 0, 0, 0);
        this.radius = 0;
        this.mass = Double.POSITIVE_INFINITY;
    }

    public Particle(double radius, double mass, double x, double y, double vx, double vy,double dt) {
        this.id_class = id_class++;
        this.radius = radius;
        this.mass = mass;
        currentState = new State(x, y, vx, vy, 0, 0);
        double prevX = x - vx * dt;
        double prevY = y - vy * dt;
        previousState = new State(prevX, prevY, vx, vy, 0, 0);
    }

    public Particle(double radius, double mass, double x, double y, double vx, double vy){
        this.id_class = id_class++;
        this.radius = radius;
        this.mass = mass;
        currentState = new State(x, y, vx, vy, 0, 0);
        previousState = new State(0, 0, 0, 0, 0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Particle particle = (Particle) o;

        return id_class.equals(particle.id_class);
    }

    @Override
    public int hashCode() {
        return id_class.hashCode();
    }

    public Long getId() {
        return id_class;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public State getPreviousState() {
        return previousState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setFutureState(State futureState){
        this.futureState = futureState;
    }

    public void updateState(){
        this.previousState = this.currentState;
        this.currentState = this.futureState;
        this.futureState = null;
    }

    public Double getX(){
        return currentState.getX();
    }

    public Double getY(){
        return currentState.getY();
    }

    public Double getvX(){
        return currentState.getvX();
    }

    public Double getvY(){
        return currentState.getvY();
    }

    public Double getaX(){
        return currentState.getaX();
    }

    public Double getaY(){
        return currentState.getaY();
    }

    public Point getPosition(){ return new Point(getX(),getY());}
    public Point getPreviousPosition(){ return new Point(getPreviousState().getX(),getPreviousState().getY());}

    public void setX(double x ){
        this.currentState.setX(x);
    }

    public void setY(double y){
        this.currentState.setY(y);
    }

    public javafx.geometry.Point2D point2D(){
        return new javafx.geometry.Point2D(getX(),getY());
    }

}
