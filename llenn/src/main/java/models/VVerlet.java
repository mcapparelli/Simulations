package models;

import Algorithm.Force;

import java.util.List;

public class VVerlet extends Integrator {

    public VVerlet(Double dt, ForceFunction forceFunction) {
        super(dt, forceFunction);
    }

    @Override
    public void moveParticle(Particle particle, Double time, List<Particle> neighbors) {
        if (forceApplied instanceof Force){
            Point force = forceApplied.getForce(new Point(particle.getX(),particle.getY()), new Point(particle.getvX(), particle.getvY()),neighbors);
            Point predictedPosition = particle.getPosition().multiply(2d).add(particle.getPreviousPosition().multiply(-1d)).add(force.multiply(dt*dt/particle.getMass()));
            Point predictedVelocity = predictedPosition.add(particle.getPreviousPosition().multiply(-1d)).multiply(1d/(2d*dt));

            particle.setFutureState(new State(
                    predictedPosition.getX(), predictedPosition.getY(),
                    predictedVelocity.getX(), predictedVelocity.getY(),
                    0d,0d
            ));

        }else{
            Point force = forceApplied.getForce(new Point(particle.getX(),particle.getY()), new Point(particle.getvX(), particle.getvY()),neighbors);
            Double x = particle.getX() + dt*particle.getvX() + dt*dt/particle.getMass()*force.getX();
            Double y = particle.getY() + dt*particle.getvY() + dt*dt/particle.getMass()*force.getY();

            Particle predictedParticle = new Particle(particle.getRadius(), particle.getMass(), x,y, particle.getvX(),particle.getvY());
            Point predictedForce = forceApplied.getForce(new Point(predictedParticle.getX(), predictedParticle.getY()),
                    new Point(predictedParticle.getvX(), predictedParticle.getvY()),neighbors);

            Double vX = particle.getvX() + dt*(force.getX() + predictedForce.getX())/(2*particle.getMass());
            Double vY = particle.getvY() + dt*(force.getY() + predictedForce.getY())/(2*particle.getMass());
            particle.setFutureState(new State(
                    x,y,vX,vY
            ));
        }
    }
}
