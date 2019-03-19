package ar.edu.itba;

import java.util.Map;
import java.util.Set;

public class Main {

    // /Users/segundofarina/TP/TP-SS/TP/CellIndexMethod/target/classes/randStatic.txt /Users/segundofarina/TP/TP-SS/TP/CellIndexMethod/target/classes/randDyn.txt /Users/segundofarina/TP/TP-SS/out
    // /Users/martin/Documents/ITBA/SimulacionDeSistemas/TP1/SimulacionDeSistemas/CellIndexMethod/target/classes/staticfile.txt /Users/martin/Documents/ITBA/SimulacionDeSistemas/TP1/SimulacionDeSistemas/CellIndexMethod/target/classes/dynamicfile.txt /Users/martin/Documents/ITBA/SimulacionDeSistemas/TP1/SimulacionDeSistemas/out
    public static void main (String[] args) {
        IO IO = new IO(args[0],args[1]);
        int L = IO.getL();
        int N = IO.getN();
        int M = IO.getM();
        double Rc = IO.getRc();
        boolean periodic = IO.isPeriodic();

        System.out.println(periodic);
        Set<Particle> particles = IO.getParticles();
        Engine engine = new Engine(L,M,Rc,periodic, particles);

        long start = System.currentTimeMillis();
        Map<Particle,Set<Particle>> ans = engine.start();
        //Map<Particle,Set<Particle>> ans = engine.bruteForce(particles);
        long end = System.currentTimeMillis();

        System.out.println(end-start);

        for(Particle particle :ans.keySet()){
            String toWrite = Engine.generateFileString(particle,ans.get(particle), particles);
            Engine.writeToFile(toWrite, particle.getId(),args[2]);
        }
        for(Map.Entry<Particle,Set<Particle>> a : ans.entrySet()){
            System.out.print(a.getKey().getId()+" : ");
            for(Particle m: a.getValue()){
                System.out.print(m.getId()+" ");
            }
            System.out.println();
        }
    }
}
