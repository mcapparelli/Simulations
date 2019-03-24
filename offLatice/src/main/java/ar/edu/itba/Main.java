package ar.edu.itba;

import java.util.*;

import static ar.edu.itba.IO.writeToFileOffLattice;

public class Main {
    private static Double VELOCITY = 0.03;
    private static Double NOISE = 4.5;
    private static int DEGREES = 360;
    private static Long SEED_POSITION = new Long(982347210);
    private static Long SEED_ANGLE = new Long(237239823);

    public static void main(String[] args){
        //Vamos a recibir un estatico y generar un dinamico y luego lo utilizamos para simular.
        startSimulation(args[0], args[1], NOISE);
    }

    private static Set<Particle> updateParticles(int L, double noise, Map<Particle, Set<Particle>> neighbours) {
        Set<Particle> newParticles = new HashSet<>();
        for (Particle m : neighbours.keySet()) {
            double newAngle = getAngleFromNeighbours(m, neighbours.get(m), noise);
            Point newLocation = getNewPosition(m, newAngle, L);
            Particle newParticle = new Particle(m.getId(), m.getRadio(), newLocation, m.getVelocity(), newAngle);
            newParticles.add(newParticle);
        }
        return newParticles;
    }

    public static void startSimulation(String staticPath, String outPath, double noise) {
        IO IO = new IO(staticPath);
        int L = IO.getL();
        Set<Particle> particles = IO.getCleanParticles();

        //Generamos el dinamico a partir de una semilla. Va a devolver siempre las mismas condiciones iniciales.
        generateDinamic(particles, L);

        Processor processor = new Processor(L, L, IO.getRc(), IO.isPeriodic(), particles);
        Map<Particle, Set<Particle>> neighbors = processor.start();

        final int time = 2000;
        long start = System.currentTimeMillis();
        double va = 0;

        StringBuilder builder = new StringBuilder();
        StringBuilder builderTimeVa = new StringBuilder();

        IO.generateFileOffLattice(particles, builder);
        IO.generateFileTimeVa(time,va,builderTimeVa);

        //la condicion de corte no es va = 1 siempre.
        for(int i = 1; i < time; i++) {
            //Particulas updeptiadas
            Set<Particle> newParticles =  updateParticles(L, noise, neighbors);
            va = calculateVa(newParticles);

            IO.generateFileOffLattice(newParticles, builder);
            builderTimeVa.append(i + "\t" + va + "\r\n");

            processor = new Processor(L, L, IO.getRc(), IO.isPeriodic(), newParticles);
            neighbors = processor.start();
        }
        writeToFileOffLattice("offLatticeRuido:" + NOISE + "N:" + IO.getN(), builder.toString(), outPath);
        writeToFileOffLattice("timeVaRuido:" + NOISE + "N:" + IO.getN(), builderTimeVa.toString(), outPath);

        long end = System.currentTimeMillis();
        System.out.println("time: " + (end - start) + "ms.");
    }

    private static void generateDinamic(Set<Particle> particles, double L) {
        Random randomPosition = new Random(SEED_POSITION);
        Random randomAngle = new Random(SEED_ANGLE);

        for(Particle particle : particles){
            particle.setLocation(new Point(randomPosition.nextDouble() * L, randomPosition.nextDouble() * L));
            particle.setAngle(randomAngle.nextDouble() * DEGREES);
            particle.setVelocity(VELOCITY);
            //System.out.println(particle.getId() + " " + particle.getLocation().getX() +  " " + particle.getLocation().getY()
            //                   + " " + particle.getAngle());
        }
    }

    private static Point getNewPosition(Particle m, double newAngle, int L) {
        double x = m.getLocation().getX()+m.getVelocity()*Math.cos(newAngle);
        double y = m.getLocation().getY()+m.getVelocity()*Math.sin(newAngle);
        x = x%L;
        y = y%L;
        if(y<0){
            y+=L;
        }
        if(x<0){
            x+=L;
        }
        return new Point(x,y);
    }

    private static double getAngleFromNeighbours(Particle m, Set<Particle> neighbours, double noise) {
        double sinTotal = Math.sin(m.getAngle());
        double cosTotal = Math.cos(m.getAngle());
        for(Particle n : neighbours) {
            sinTotal += Math.sin(n.getAngle());
            cosTotal += Math.cos(n.getAngle());
        }
        double n= new Random().nextDouble()*noise-noise/2;
        return Math.atan2(sinTotal/(neighbours.size() + 1), cosTotal/(neighbours.size() + 1) )+n;
    }

    private static double calculateVa(Set<Particle> particles) {
        double totalVx = 0;
        double totalVy = 0;
        for(Particle m : particles) {
            totalVx += m.getVelocity() * Math.cos(m.getAngle());
            totalVy += m.getVelocity() * Math.sin(m.getAngle());
        }
        totalVx /= particles.size();
        totalVy /= particles.size();
        double totalVi = Math.sqrt(Math.pow(totalVx, 2) + Math.pow(totalVy, 2));
        return totalVi / (VELOCITY);
    }
}
