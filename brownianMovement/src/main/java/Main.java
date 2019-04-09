import ar.edu.itba.ParticleWithMass;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<ParticleWithMass> particles = ParticleWithMass.generateParticles(new Long(123456), 3, 0.5, 0.005, 0.1, 0.1);
        Simulation simulation = new Simulation(0.5, particles);
        simulation.startForAnimation(20,"/Users/Cappa/ITBA/simulations/Results/animation");

        //simulation.startUntilCrash("/Users/Cappa/ITBA/simulations/Results/untilCrash");
        //simulation.startForTime(200,"/Users/Cappa/ITBA/simulations/Results/forTime");
        //simulation.startForAnimation(200,"/Users/Cappa/ITBA/simulations/Results/heat");
        //double vel = 0.01;
        //do {
        //    Set<ParticleWithMass> particles = ParticleWithMass.generateParticles(new Long(123456), 499, 0.5, 0.005, 0.1, vel);
        //    Simulation simulation = new Simulation(0.5, particles);
        //   simulation.start(1000, "/Users/Cappa/ITBA/simulations/Results/heat");
        //    vel = vel + vel/3;
        //}while (vel != 0.1);
    }
}
