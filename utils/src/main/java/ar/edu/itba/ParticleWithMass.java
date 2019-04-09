package ar.edu.itba;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

public class ParticleWithMass {
    private static int id = 0;
    private int properId;
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
        properId = id++;
    }

    public int getId() {
        return properId;
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

    public static Set<ParticleWithMass> generateParticles(Long seed, int n, double l, double smallRadius, double smallMass, double vel) {
        Random randVel = new Random(seed);
        Random randPos = new Random(seed+9654);
        Set<ParticleWithMass> particlesSet = new HashSet<>();
        ParticleWithMass bigParticle = new ParticleWithMass(l/2,l/2,0.0,0.0, 0.05, 100);
        particlesSet.add(bigParticle);
        if(n > 0){
            do {
                double xpos = l * randPos.nextDouble();
                double ypos = l * randPos.nextDouble();

                double velAngle = randVel.nextDouble() * 2 * Math.PI;
                double velVariation = vel * randVel.nextDouble();

                double xvel = velVariation * cos(velAngle);
                double yvel = velVariation * sin(velAngle);

                ParticleWithMass particleWithMass = new ParticleWithMass(xpos, ypos, xvel, yvel, smallRadius, smallMass);
                if(!existParticleInPosition(xpos,ypos,smallRadius, particlesSet))
                    particlesSet.add(particleWithMass);
            } while (particlesSet.size() != n + 1);

        }
        return particlesSet;
    }

    private static boolean existParticleInPosition(double xPosition, double yPosition, double radius, Set<ParticleWithMass> particles) {
        for(ParticleWithMass p : particles) {
            if( Math.pow((p.getxPosition() - xPosition),2) + Math.pow((p.getyPosition() - yPosition),2) <= Math.pow((p.getRadius() + radius),2) ) {
                return true;
            }
        }
        return false;
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
