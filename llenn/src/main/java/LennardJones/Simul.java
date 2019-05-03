package LennardJones;

import Integrators.Integrator;
import Integrators.NeighborDetection;
import io.Input;
import io.Output;
import models.Grid;
import models.Particle;
import models.Wall;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Simul {

    Input input;
    double gapStart;
    double gapEnd;
    Integrator currentAlgotithm;
    Force lennardJonesForceCalcuator;

    public Simul(double dt, Integrator integrator)
    {
        this.input = new Input(Long.valueOf(1000),dt);
        this.lennardJonesForceCalcuator = new Force(input.getRm(), input.getEpsilon());
        this.currentAlgotithm = integrator;
        gapStart = (input.getBoxHeight() / 2) - (input.getOrificeLength() / 2);
        gapEnd = input.getBoxHeight()- gapStart;
    }


    public void simulate(double dt) throws IOException {

        double time = 0.0;
        int iteration = 0;
        List<Particle> particles = input.getParticles();
        int leftParticles=1000;
        long start = System.currentTimeMillis();
        while (leftParticles>500) {
            Grid grid = new Grid(input.getCellSideQuantity(), input.getSystemSideLength());
            grid.setParticles(particles);
            Map<Particle, List<Particle>> neighbours = NeighborDetection.getNeighbors(grid, grid.getUsedCells(), input.getInteractionRadio(), false);

            double auxtime = time;
            neighbours.entrySet().stream().parallel().forEach(particle -> move(particle.getKey(), particle.getValue(), auxtime));

            particles.stream().parallel().forEach(Particle::updateState);
            if (iteration % 10000 == 0){
                leftParticles = Output.printParticle(particles,((int)(time*10))/10.0);
                System.out.println(time +" " + leftParticles );
                Output.printToFile(particles);
            }
            if (((int)((time % 0.1)*100000)) == 0) {
                Output.printVelocities(particles, time);
            }
            time += dt;
            iteration++;
        }
        System.out.println(System.currentTimeMillis() - start);

    }

    private void move(Particle p, List<Particle> neighbours, Double time){
        neighbours = neighbours.stream().filter(n -> !isWallBetween(p, n)).collect(Collectors.toList());
        addWall(p,neighbours);
        currentAlgotithm.moveParticle(p, time, neighbours); //Update States
    }


    private void addWall(Particle p, List<Particle> neighbours ){
        int up = input.getBoxHeight();
        int right = input.getBoxWidth();
        double ir = input.getInteractionRadio();
        //TOP
        if(up - p.getY() <= ir)
            neighbours.add(new Particle(Wall.typeOfWall.TOP.getVal(),p.getX(),up));
        //BOTTOM
        if(p.getY()<=ir)
            neighbours.add(new Particle(Wall.typeOfWall.BOTTOM.getVal(),p.getX(),0));



        double distancesToMiddle = input.getMiddleBoxWidth() - p.getX();
        if(distancesToMiddle<=0){
            distancesToMiddle = Math.abs(distancesToMiddle);
            if ( distancesToMiddle <= ir && notGap(p) )
                neighbours.add(new Particle(Wall.typeOfWall.MIDDLE.getVal(),input.getMiddleBoxWidth(),p.getY()));
            else if ( right - p.getX() <= ir )
                neighbours.add(new Particle(Wall.typeOfWall.RIGHT.getVal(),input.getBoxWidth(),p.getY()));
        }else {
            if ( distancesToMiddle <= ir && notGap(p) )
                neighbours.add(new Particle(Wall.typeOfWall.MIDDLE.getVal(),input.getMiddleBoxWidth(),p.getY()));
            else if(p.getX()<=ir)
                neighbours.add(new Particle(Wall.typeOfWall.LEFT.getVal(),0,p.getY()));
        }

        if(!notGap(p)){
            Particle pGapStart = new Particle(Wall.typeOfWall.GAPSTART.getVal(),input.getMiddleBoxWidth(),gapStart);
            Particle pGapEnd = new Particle(Wall.typeOfWall.GAPEND.getVal(),input.getMiddleBoxWidth(),gapEnd);
            if(getDistances(p,pGapStart)<=ir)
                neighbours.add(pGapStart);
            if (getDistances(p,pGapEnd)<= ir)
                neighbours.add(pGapEnd);
        }

    }

    private boolean isWallBetween(Particle p1, Particle p2) {
        double x1= p1.getX();
        double y1= p1.getY();
        double x2= p2.getX();
        double y2= p2.getY();

        if (x1 == x2) {
            return false;
        }

        final double m = (y2 - y1) / (x2 - x1);
        final double b = y1 - m * x1;
        final double xp = input.getBoxWidth() / 2;
        final double yp = m * xp + b;

        return yp > gapStart && yp < gapEnd &&
                ((x1 > input.getBoxWidth() / 2 && x2 < input.getBoxWidth()/ 2) ||
                        (x1 < input.getBoxWidth() / 2 && x2 > input.getBoxWidth() / 2));
    }

    private boolean notGap(Particle p){
        return (p.getY() <= gapStart || p.getY() >= gapEnd);
    }

    private double getDistances(Particle p1, Particle p2){
        double y = Math.abs(p2.getY() - p1.getY());
        double x = Math.abs(p2.getX() - p1.getX());
        return Math.hypot(y, x);
    }

}
