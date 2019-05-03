import Integrators.Integrator;
import Integrators.VVerlet;
import LennardJones.Force;
import LennardJones.Simul;

import java.io.IOException;

public class Main {

    static final double DEFAULT_DT = 0.001;

    public static void main(String[] args) throws IOException {
        Force l = new Force(1.0,2.0);
        Integrator i= new VVerlet(DEFAULT_DT,l);
        Simul simulation = new Simul(DEFAULT_DT,i);
        simulation.simulate(DEFAULT_DT);
    }
}
