import ar.edu.itba.IO;
import ar.edu.itba.ParticleWithMass;
import ar.edu.itba.Result;

import java.util.LinkedList;
import java.util.List;

public class Simulation {

    public static void main(String[] args) {
        final double MAX_TIME = 5.1;
        double mass = 70.0;
        double gamma = 100.0;
        double amplitude = 3.0;
        double k = Math.pow(10,4);
        double time = 0.0;
        double delta = 0.001;

        ParticleWithMass particle = new ParticleWithMass(1.0, 0.0, -amplitude * gamma/2 * mass,
                0.0, 1.0, mass);
        DumpledOscillatorSolution solution = new DumpledOscillatorSolution(amplitude);
        List<Result> results = new LinkedList<Result>();
        StringBuilder builder = new StringBuilder();

        while (time < MAX_TIME){
            System.out.println(time);
            Result r = new Result(solution.getPosition(particle,gamma,k,time),0.0,0.0,0.0,time);
            results.add(r);
            time = time + delta;
        }

        IO.generateFileOscillator(builder,results);
        IO.writeToFileOscillator("OscillatorDelta" + delta, builder.toString(), "./results");
    }

}
