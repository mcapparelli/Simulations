package ar.edu.itba;

import java.util.*;

public class Grid {
    private int l;
    private int m;
    private double rc;
    private double fieldSize;
    private HashMap<Point,Set<Particle>> grid;
    private boolean periodic;

    public Grid(int l, int m, double rc, Set<Particle> particles, boolean periodic) {
        this.l = l;
        this.m = m;
        this.rc = rc;
        this.fieldSize = (double)l/m;
        this.grid = new HashMap<>();
        this.periodic = periodic;
        for(int i = 0; i < m ; i++){
            for(int j = 0 ; j < m ; j++) {
                grid.put(new Point(i, j), new HashSet<Particle>());
            }
        }
        for(Particle particle : particles){
            Point cell = getField(particle.getLocation());
            try {
                grid.get(cell).add(particle);
            }catch (Exception e){
                System.out.println(cell + " " + particle);
            }
        }
    }

    private Point getField(Point location){
        int x = (int)(location.getX()/fieldSize);
        int y = (int)(location.getY()/fieldSize);
        return new Point(x,y);
    }

    public Set<Particle> getParticlesInCell(int x , int y){
        if(x < 0){
            if(periodic) {
                return getParticlesInCell(x + m, y);
            }else {
                return Collections.EMPTY_SET;
            }
        } if(x >= m) {
            if(periodic) {
                return getParticlesInCell(x - m, y);
            } else {
                return Collections.EMPTY_SET;
            }
        }
        if(y < 0) {
            if(periodic) {
                return getParticlesInCell(x,y + m);
            } else {
                return Collections.EMPTY_SET;
            }
        }
        if(y >= m){
            if(periodic) {
                return getParticlesInCell(x,y - m);
            } else {
                return Collections.EMPTY_SET;
            }
        }
        return grid.get(new Point(x,y));
    }

    private Set<Particle> getNearParticles(Point field){
        Set<Particle> nearParticles = new HashSet<>();
        nearParticles.addAll(getParticlesInCell((int)field.getX(),         (int) field.getY()));
        nearParticles.addAll(getParticlesInCell((int)field.getX()+1,    (int) field.getY()));
        nearParticles.addAll(getParticlesInCell((int)field.getX()+1, (int) field.getY()+1));
        nearParticles.addAll(getParticlesInCell((int)field.getX(),      (int) field.getY()+1));
        nearParticles.addAll(getParticlesInCell((int)field.getX()+1, (int) field.getY()-1));
        return nearParticles;
    }

    public Set<Particle> getParticleNeighbors(Particle particle, Set<Particle> analyzed, Set<Particle> nearParticles){
        final Point filed = getField(particle.getLocation());
        //Set<Particle> nearParticles = getNearParticles(filed);
        //nearParticles.remove(particle);
        //nearParticles.removeAll(analyzed);
        Set<Particle> ans = new HashSet<>();
        for(Particle other : nearParticles){
            if(!other.equals(particle) && !analyzed.contains(other)) {
                if(calculateDistanceParticles(particle,other)){
                    ans.add(other);
                }
            }
        }
        return ans;
    }

    private boolean calculateDistanceParticles(Particle particle, Particle other) {
        if(Particle.distanceBetweenParticles(particle,other) - particle.getRadio() - other.getRadio() <= rc) {
            return true;
        }

        if(!periodic){
            return false;
        }

        double x1 = particle.getLocation().getX();
        double y1 = particle.getLocation().getY();
        double x2 = other.getLocation().getX();
        double y2 = other.getLocation().getY();

        return
           Point.distanceBetween(new Point(x1,y1 + l),        new Point(x2,y2)) - particle.getRadio() - other.getRadio() <= rc
        || Point.distanceBetween(new Point(x1 + l, y1),       new Point(x2,y2)) - particle.getRadio() - other.getRadio() <= rc
        || Point.distanceBetween(new Point(x1 + l,y1 + l), new Point(x2,y2)) - particle.getRadio() - other.getRadio() <= rc
        || Point.distanceBetween(new Point(x1 + l,y1 - l), new Point(x2,y2)) - particle.getRadio() - other.getRadio() <= rc
        || Point.distanceBetween(new Point(x1,y1 - l),        new Point(x2,y2)) - particle.getRadio() - other.getRadio() <= rc
        || Point.distanceBetween(new Point(x1 - l,y1 - l), new Point(x2,y2)) - particle.getRadio() - other.getRadio() <= rc
        || Point.distanceBetween(new Point(x1 - l, y1),        new Point(x2,y2)) - particle.getRadio() - other.getRadio() <= rc
        || Point.distanceBetween(new Point(x1 - l,y1 + l), new Point(x2,y2)) - particle.getRadio() - other.getRadio() <= rc;
    }

    public Map<Particle,Set<Particle>> processCell(Point cell){
        Set<Particle> particles = grid.get(cell);
        Set<Particle> nearCellParticles = getNearParticles(cell);
        Set<Particle> analyzed = new HashSet<>();
        Map<Particle,Set<Particle>> map = new HashMap<>();

        for(Particle particle : particles){
            Set<Particle> neighbors = getParticleNeighbors(particle, analyzed, nearCellParticles);
            analyzed.add(particle);
            map.put(particle,neighbors);
        }
        return map;
    }

    public double getRc() {
        return rc;
    }
}
