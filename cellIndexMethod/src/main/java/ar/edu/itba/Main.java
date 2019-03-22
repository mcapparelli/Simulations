package ar.edu.itba;

import java.util.Map;
import java.util.Set;

public class Main {

    public static void main (String[] args) {
        IO IO = new IO(args[0], args[1]);
        int L = IO.getL();
        int M = IO.getM();
        double Rc = IO.getRc();
        boolean periodic = IO.isPeriodic();

        System.out.println(periodic);
        Set<Particle> particles = IO.getParticles();
        Processor processor = new Processor(L,M,Rc,periodic, particles);

        long start = System.currentTimeMillis();
        Map<Particle,Set<Particle>> ans = processor.start();
        //Map<Particle,Set<Particle>> ans = processor.bruteForce(particles);
        long end = System.currentTimeMillis();

        System.out.println(end-start);

        for(Particle particle :ans.keySet()){
            String toWrite = IO.generateFileString(particle, ans.get(particle), particles);
            Processor.writeToFile(toWrite, particle.getId(),args[2]);
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
