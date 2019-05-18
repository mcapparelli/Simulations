package models;

import models.Integrator;
import models.VVerlet;
import Algorithm.Force;
import Algorithm.Simul;
import java.io.IOException;

public class Main {

    static double dt = 0.001;

    public static void main(String[] args) throws IOException {
        Force l = new Force(1.0,2.0);
        Integrator i= new VVerlet(dt,l);
        Simul simulation = new Simul(dt,i);
        simulation.simulate(dt);
    }
}
