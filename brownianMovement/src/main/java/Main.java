import ar.edu.itba.ParticleWithMass;

import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Set<ParticleWithMass> particles = ParticleWithMass.generateParticles(new Long(12306), 200, 0.5, 0.005, 0.1, 0.1);
        //Simulation sim = new Simulation(0.5, particles);
        //sim.startForAnimation(600,"/Users/Cappa/ITBA/simulations/Results/animation");
        //for(int i = 1; i < 11 ; i++){
        Simulation simulation = new Simulation(0.5, particles);
        simulation.StartAndStopWhenBigParticleCrashes("/Users/Cappa/ITBA/simulations/Results/heat");
        //}

        //simulation.StartAndStopWhenBigParticleCrashes("/Users/Cappa/ITBA/simulations/Results/untilCrash");
        //simulation.startWithTimeSet(200,"/Users/Cappa/ITBA/simulations/Results/forTime");
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
