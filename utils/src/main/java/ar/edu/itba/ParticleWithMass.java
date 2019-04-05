package ar.edu.itba;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

public class ParticleWithMass {
    private static int id = 0;
    private double xPosition;
    private double yPosition;
    private double xSpeed;
    private double ySpeed;
    private double radius;
    private double mass;
    private int crashesAmount;

    public ParticleWithMass(double xPosition, double yPosition, double xSpeed, double ySpeed, double radius, double mass) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.radius = radius;
        this.mass = mass;
        this.crashesAmount = 0;
        this.id = id++;
    }

    public int getId() {
        return id;
    }


    public double getxPosition() {
        return xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public double getRadius() {
        return radius;
    }

    public void addCrash(){
        crashesAmount++;
    }

    public int getCrashesAmount(){
        return crashesAmount;
    }

    public double getMass() {
        return mass;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public Set<ParticleWithMass> generateParticles(Long seed, int n, double l, double smallRadius, double smallMass, double vel) {
        int counter = n;
        Random randVel = new Random(seed);
        Random randPos = new Random(seed+9654);
        Set<ParticleWithMass> particlesSet = new HashSet<>();
        if(n > 0){
            do {
                double xpos = l * randPos.nextDouble();
                double ypos = l * randPos.nextDouble();

                double velAngle = randVel.nextDouble() * 360;
                double velVariation = vel * randVel.nextDouble();

                double xvel = velVariation * cos(velAngle);
                double yvel = velVariation * sin(velAngle);

                ParticleWithMass particleWithMass = new ParticleWithMass(xpos, ypos, xvel, yvel, smallRadius, smallMass);
                particlesSet.add(particleWithMass);
                counter--;
            } while (counter > 0);

            ParticleWithMass bigParticle = new ParticleWithMass(l/2,l/2,0.0,0.0, 0.05, 100);
            particlesSet.add(bigParticle);
        }
        return particlesSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticleWithMass particleWithMass = (ParticleWithMass) o;
        return Double.compare(particleWithMass.xPosition, xPosition) == 0 &&
                Double.compare(particleWithMass.yPosition, yPosition) == 0 &&
                Double.compare(particleWithMass.xSpeed, xSpeed) == 0 &&
                Double.compare(particleWithMass.ySpeed, ySpeed) == 0 &&
                Double.compare(particleWithMass.radius, radius) == 0 &&
                Double.compare(particleWithMass.mass, mass) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPosition, yPosition, xSpeed, ySpeed, radius, mass);
    }

    @Override
    public String toString() {

        return "ar.edu.itba.ParticleWithMass{" +
                "id=" + id +
                ", xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                ", xSpeed=" + xSpeed +
                ", ySpeed=" + ySpeed +
                ", radius=" + radius +
                ", mass=" + mass +
                '}';
    }

}
