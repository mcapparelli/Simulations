import ar.edu.itba.ParticleWithMass;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

public class Simulation {
    private double boxSize;
    private Set<ParticleWithMass> particles;
    private BufferedWriter bw;
    private PriorityQueue<Collision> queue;

    private final double largeRadius = 0.05; // meters
    private double time;


    public Simulation(double boxSize, Set<ParticleWithMass> particles) {
        this.boxSize = boxSize;
        this.particles = new HashSet<>();
        this.bw=null;
        this.queue = new PriorityQueue<>();
        this.time =0;
        this.particles = particles;
    }


    public void start(int iterations, String outPath) {
        long start= System.currentTimeMillis();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        if (!initalizeBW(outPath)) return;

        appendToFile(bw,"Time between collisions: \n");
        appendToFile(bw,"particles: " +particles.size()+ " iterations: "+iterations+"\n");

        calculateNextCrashTimeForEveryone();

        for(int i = 0; i < iterations; i++) {
            Collision nextCollision = queue.poll();
            updateParticlesPosition(nextCollision.getTime()-time);
            updateSpeedCrashedParticles(nextCollision.getParticles());
            appendToFile(bw,df.format((nextCollision.getTime()-time))+"\n");
            System.out.println(new Double(nextCollision.getTime()-time));
            time=nextCollision.getTime();
            for(ParticleWithMass p : nextCollision.getParticles()){
                updateQueue(p);
            }
        }
        long end = System.currentTimeMillis();

        appendToFile(bw,"Simulated time: "+time+"s\n");
        appendToFile(bw,"Proccesing time:"+(end-start)+"ms\n");

        System.out.println("Proccesing time:"+(end-start)+"ms");
        System.out.println("Simulated time: "+time+"s");
        closeBW();
    }

    public void StartAndStopWhenBigParticleCrashes(String outPath) {
        long start= System.currentTimeMillis();
        double nextTime =1;
        int collisions =0;

        if (!initalizeBW(outPath)) return;

        appendToFile(bw,"X\tY\r\n");//"+particles.size()+ " end UntilCrash\n");

        StringBuilder builder = new StringBuilder();
        ParticleWithMass bigParticle = particles.stream()
                                                .filter(bigBall -> bigBall.getRadius() == largeRadius)
                                                .findAny()
                                                .get();
        ParticleWithMass aParticle = particles.stream()
                                              .filter(bigBall -> bigBall.getId() == 40)
                                              .findAny()
                                              .get();
        builder.append(aParticle.getxPosition() + "\t" + aParticle.getyPosition() + "\n");
        appendToFile(bw,builder.toString());

        calculateNextCrashTimeForEveryone();
        Long counter = new Long(0);
        while(true) {
            Collision nextCollision = queue.poll();
            updateParticlesPosition(nextCollision.getTime()-time);
            updateSpeedCrashedParticles(nextCollision.getParticles());
            time=nextCollision.getTime();
            //appendToFile(bw,collisions+ "\t"+nextTime+" s\n");
            if(time > nextTime){
                System.out.println(collisions+ "\t"+nextTime+" s");
                nextTime++;
                collisions=0;
            } else {
                collisions++;
            }
            for(ParticleWithMass p : nextCollision.getParticles()){
                updateQueue(p);
            }
            if(nextCollision.contains(bigParticle) && nextCollision.getParticles().size()==1){
                break;
            }
            if(counter % 10 == 0){
                ParticleWithMass aParticle2 = particles.stream()
                        .filter(bigBall -> bigBall.getId() == 40)
                        .findAny().get();
                builder.append(aParticle2.getxPosition() + "\t" + aParticle2.getyPosition() + "\n");
                appendToFile(bw,builder.toString());
            }
            counter++;
        }

        long end = System.currentTimeMillis();

        System.out.println("Simulated time: "+time+"s");
        System.out.println("Proccesing time:"+(end-start)+"ms");
        closeBW();
    }

    public void startWithTimeSet(double endTime, String outPath) {
        long start= System.currentTimeMillis();
        double nextTime =1;
        int collisions =0;
        int iterations =0;
        if (!initalizeBW(outPath)) return;
        appendToFile(bw,"particles: "+particles.size()+ " endTime: "+endTime+"\n");
        calculateNextCrashTimeForEveryone();
        while(time<endTime) {
            Collision nextCollision = queue.poll();
            updateParticlesPosition(nextCollision.getTime()-time);
            updateSpeedCrashedParticles(nextCollision.getParticles());
            time=nextCollision.getTime();
            if(time > nextTime){
                System.out.println(collisions+ "\t"+nextTime+" s");
                appendToFile(bw,collisions+ "\t"+nextTime+" s\n");
                nextTime++;
                collisions=0;
            }
            collisions++;
            for(ParticleWithMass p : nextCollision.getParticles()){
                updateQueue(p);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Simulated time: "+time+"s");
        appendToFile(bw,"Simulated time: "+time+"s\n");
        appendToFile(bw,"Proccesing time:"+(end-start)+"ms\n");
        System.out.println("Proccesing time:"+(end-start)+"ms");
        closeBW();

    }

    public void startForAnimation(int animationTime, String outPath){
        int framesPerSecond = 10;
        double jump = (double)1/framesPerSecond;
        double nextFrame = jump;
        calculateNextCrashTimeForEveryone();
        if (!initalizeBW(outPath)) return;
        while (time<animationTime){
            Collision nextCollision = queue.poll();
            if(nextCollision.getTime() > nextFrame){
                updateParticlesPosition(nextFrame-time);
                appendToFile(bw,generateFileString(particles));
                time=nextFrame;
                nextFrame+=jump;
                System.out.println(time);
                queue.add(nextCollision);
            }else{
                updateParticlesPosition(nextCollision.getTime()-time);
                updateSpeedCrashedParticles(nextCollision.getParticles());
                time=nextCollision.getTime();
                for(ParticleWithMass p : nextCollision.getParticles()){
                    updateQueue(p);
                }
            }
        }
        System.out.print("Time: "+time);
        closeBW();
    }

    private void updateQueue(ParticleWithMass particle) {
        queue.removeIf((x)-> x.contains(particle));
        double wallCrashTime = wallCrash(particle);
        if(wallCrashTime != Double.POSITIVE_INFINITY){
            queue.add(new ParticleAgainstLimits(particle,wallCrashTime+time));
        }

        for(ParticleWithMass other : particles) {
            if(!other.equals(particle)) {
                double particleCrashTime = particlesCrash(particle, other);
                if (particleCrashTime != Double.POSITIVE_INFINITY) {
                    queue.add(new ParticleAgainstParticle(particle, other, particleCrashTime+time));
                }
            }
        }
    }

    private void calculateNextCrashTimeForEveryone(){
        Set<ParticleWithMass> aux = new HashSet<>(particles);
        ParticleWithMass current = aux.iterator().next();
        aux.remove(current);
        calculateNextCrashTimeForEveryone(current,aux);
    }

    private void calculateNextCrashTimeForEveryone(ParticleWithMass p, Set<ParticleWithMass> others){
        if(others.size() > 0){
            double time = Double.POSITIVE_INFINITY;
            double wallCrashTime = wallCrash(p);

            if(wallCrashTime != Double.POSITIVE_INFINITY){
                queue.add(new ParticleAgainstLimits(p,wallCrashTime));
            }

            for(ParticleWithMass other : others) {
                    double particleCrashTime = particlesCrash(p, other);
                    if(particleCrashTime != Double.POSITIVE_INFINITY) {
                        queue.add(new ParticleAgainstParticle(p, other, particleCrashTime));
                    }
            }
            ParticleWithMass next = others.iterator().next();
            others.remove(next);
            calculateNextCrashTimeForEveryone(next,others);
        }
    }

    private double getNextCrashTime(Set<ParticleWithMass> crashedParticles) {
        double time = Double.POSITIVE_INFINITY;
        for(ParticleWithMass p : particles) {
            // get wall crash time
            double wallCrashTime = wallCrash(p);
            if(time > wallCrashTime) {
                time = wallCrashTime;
                saveCrashedParticle(crashedParticles, p);
            }

            // get other particles crash
            for(ParticleWithMass other : particles) {
                if(!other.equals(p)) {
                    double particleCrashTime = particlesCrash(p, other);
                    if(particleCrashTime < time) {
                        time = particleCrashTime;
                        saveCrashedParticle(crashedParticles, p, other);
                    }
                }
            }
        }
        return time;
    }

    static double particlesCrash(ParticleWithMass pi, ParticleWithMass pj) {
        double dvx = pj.getxSpeed()-pi.getxSpeed();
        double dvy = pj.getySpeed()-pi.getySpeed();

        double dx = pj.getxPosition()-pi.getxPosition();
        double dy = pj.getyPosition()-pi.getyPosition();

        double dvdr = dvx*dx + dvy*dy;
        if(dvdr >= 0) {
            return Double.POSITIVE_INFINITY;
        }

        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double phi = pi.getRadius()+pj.getRadius();
        double d = dvdr*dvdr - dvdv*(drdr-phi*phi);

        if(d < 0) {
            return Double.POSITIVE_INFINITY;
        }
        return -(dvdr+Math.sqrt(d))/dvdv;
    }

    private double wallCrash(ParticleWithMass p) {
        double minTimeX = Double.POSITIVE_INFINITY;
        double minTimeY = Double.POSITIVE_INFINITY;

        if(p.getxSpeed() > 0) {
            minTimeX = (boxSize - p.getRadius() - p.getxPosition()) / p.getxSpeed();
        } else if(p.getxSpeed() < 0) {
            minTimeX = (p.getRadius() - p.getxPosition()) / p.getxSpeed();
        }
        if(p.getySpeed() > 0) {
            minTimeY = (boxSize - p.getRadius() - p.getyPosition()) / p.getySpeed();

        } else if(p.getySpeed() < 0) {
            minTimeY = (p.getRadius() - p.getyPosition()) / p.getySpeed();
        }

        return (minTimeX < minTimeY) ? minTimeX : minTimeY;
    }

    private void updateParticlesPosition(double time) {
        for(ParticleWithMass p : particles) {
            p.setxPosition(p.getxPosition() + p.getxSpeed() * time);
            p.setyPosition(p.getyPosition() + p.getySpeed() * time);
        }
    }

    private void saveCrashedParticle(Set<ParticleWithMass> crashedParticles, ParticleWithMass p) {
        crashedParticles.clear();
        crashedParticles.add(p);
    }

    private void saveCrashedParticle(Set<ParticleWithMass> crashedParticles, ParticleWithMass p, ParticleWithMass o) {
        crashedParticles.clear();
        crashedParticles.add(p);
        crashedParticles.add(o);
    }

    private void updateSpeedCrashedParticles(Set<ParticleWithMass> crashedParticles) {
        Iterator<ParticleWithMass> it = crashedParticles.iterator();

        /* Crashed against wall */
        if(crashedParticles.size() == 1) {
            ParticleWithMass p = it.next();
            if(crashedAgainstVerticalWall(p)) {
                p.setxSpeed(-p.getxSpeed());
            } else {
                p.setySpeed(-p.getySpeed());
            }
        }

        /* Crashed against other particle */
        if(crashedParticles.size() == 2) {
            ParticleWithMass pi = it.next();
            ParticleWithMass pj = it.next();

            //double dVdR = (p2.getxSpeed() - p1.getxSpeed()) * (p2.getxPosition() - p2.getxPosition()) + (p2.getySpeed() - p1.getySpeed()) * (p2.getyPosition() - p2.getyPosition());
            double dvx = pj.getxSpeed()-pi.getxSpeed();
            double dvy = pj.getySpeed()-pi.getySpeed();

            double dx = pj.getxPosition()-pi.getxPosition();
            double dy = pj.getyPosition()-pi.getyPosition();

            double dvdr = dvx*dx + dvy*dy;

            double phi = pi.getRadius() + pj.getRadius();
            double J = (2 * pi.getMass() * pj.getMass() * dvdr) / ( phi * (pi.getMass() + pj.getMass()) );
            double Jx = J * (pj.getxPosition() - pi.getxPosition()) / phi;
            double Jy = J * (pj.getyPosition() - pi.getyPosition()) / phi;

            pi.setxSpeed(pi.getxSpeed() + Jx / pi.getMass());
            pi.setySpeed(pi.getySpeed() + Jy / pi.getMass());
            pj.setxSpeed(pj.getxSpeed() - Jx / pj.getMass());
            pj.setySpeed(pj.getySpeed() - Jy / pj.getMass());
        }
    }

    private boolean crashedAgainstVerticalWall(ParticleWithMass p) {
        double delta = 0.00000001;
        if(p.getxPosition()-delta <= p.getRadius()) {
            return true;
        }
        if(p.getxPosition() +delta>= boxSize - p.getRadius()) {
            return true;
        }
        return false;
    }

    private void closeBW() {
        if (bw != null)
            try {
                bw.flush();
                bw.close();
            } catch (IOException ioe2) { }
    }

    private boolean initalizeBW(String outPath) {
        try {
            bw = new BufferedWriter(new FileWriter(outPath+"/eventDriven" + LocalDateTime.now() + ".txt", true));
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String generateFileString(Set<ParticleWithMass> allParticles){
        StringBuilder builder = new StringBuilder()
                .append(allParticles.size())
                .append("\r\n")
                .append("//ID\t X\t Y\t Radius\t Vx\t Vy\t\r\n");
        appendParticles(allParticles, builder);
        return builder.toString();
    }

    private void appendParticles(Set<ParticleWithMass> allParticles, StringBuilder builder) {
        for(ParticleWithMass current: allParticles){
            double vx = current.getxSpeed();
            double vy = current.getySpeed();
            builder.append(current.getId())
                    .append(" ")
                    .append(current.getxPosition())
                    .append(" ")
                    .append(current.getyPosition())
                    .append(" ")
                    .append(current.getRadius())
                    .append(" ")
                    .append(new Double(vx).floatValue())
                    .append(" ")
                    .append(new Double(vy).floatValue())
                    .append("\r\n");
        }
    }

    public static void appendToFile (BufferedWriter bw , String data) {
        try {
            bw.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
