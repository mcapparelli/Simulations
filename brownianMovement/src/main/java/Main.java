import ar.edu.itba.ParticleWithMass;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<ParticleWithMass> particles = ParticleWithMass.generateParticles(new Long(123456), 500, 0.5, 0.005, 0.1, 0.1);
        Simulation simulation = new Simulation(0.5, particles);

        //s.startForTime(200,"/Users/Cappa/ITBA/simulations/Results");
        //s.startUntilCrash("/Users/Cappa/ITBA/simulations/Results");
        simulation.startForAnimation(100,"/Users/Cappa/ITBA/simulations/Results");
    }
}
