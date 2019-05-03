package Integrators;


import models.ForceFunction;
import models.Particle;

import java.util.Collections;
import java.util.List;

public abstract class Integrator {
    Double dt;
    ForceFunction forceApplied;

    public Integrator(Double dt, ForceFunction forceFunction) {
        this.dt = dt;
        this.forceApplied = forceFunction;
    }

    public abstract void moveParticle(Particle particle, Double time, List<Particle> neighbors);

    public Double unidimensionalNextPosition(Particle particle, Double time) {
        moveParticle(particle, time, Collections.emptyList());
        particle.updateState();
        return particle.getY();
    }
}
