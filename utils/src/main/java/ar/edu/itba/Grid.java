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
            Point j = getField(particle.getLocation());
            try {
                grid.get(j).add(particle);
            }catch (Exception e){
                System.out.println(j + " " + particle);
            }
        }

    }

    private Point getField(Point location){
        int x = (int)(location.getX()/fieldSize);
        int y = (int)(location.getY()/fieldSize);
        return new Point(x,y);
    }

    public Set<Particle> getMoleculesInCell(int x , int y){
        if(x < 0){
            if(periodic) {
                return getMoleculesInCell(x+m,y);
            }else {
                return Collections.EMPTY_SET;
            }
        } if(x >= m) {
            if(periodic) {
                return getMoleculesInCell(x-m,y);
            }else {
                return Collections.EMPTY_SET;
            }
        }
        if(y < 0) {
            if(periodic) {
                return getMoleculesInCell(x,y+m);
            }else {
                return Collections.EMPTY_SET;
            }
        }
        if(y >= m){
            if(periodic){
                return getMoleculesInCell(x,y-m);
            }else{
                return Collections.EMPTY_SET;
            }
        }
        return grid.get(new Point(x,y));
    }

    private Set<Particle> getNearMolecules(Point field){
        Set<Particle> nearParticles = new HashSet<>();
        nearParticles.addAll(getMoleculesInCell((int)field.getX(),         (int) field.getY()));
        nearParticles.addAll(getMoleculesInCell((int)field.getX()+1,    (int) field.getY()));
        nearParticles.addAll(getMoleculesInCell((int)field.getX()+1, (int) field.getY()+1));
        nearParticles.addAll(getMoleculesInCell((int)field.getX(),      (int) field.getY()+1));
        nearParticles.addAll(getMoleculesInCell((int)field.getX()+1, (int) field.getY()-1));
        return nearParticles;
    }

    public Set<Particle> getNeighborsOfMolecule(Particle particle, Set<Particle> analyzed, Set<Particle> nearParticles){
        final Point filed = getField(particle.getLocation());
        //Set<Particle> nearParticles = getNearMolecules(filed);
        //nearParticles.remove(particle);
        //nearParticles.removeAll(analyzed);
        Set<Particle> ans = new HashSet<>();
        for(Particle other : nearParticles){
            if(!other.equals(particle) && !analyzed.contains(other)) {
                if(calculateDistanceBetweenMolecules(particle,other)){
                    ans.add(other);
                }
            }
        }
        return ans;
    }

    private boolean calculateDistanceBetweenMolecules(Particle particle, Particle other) {

        if(Particle.distanceBetweenMolecules(particle,other)- particle.getRatio()-other.getRatio()<=rc) {
            return true;
        }
        if(!periodic){
            return false;
        }
        double mx = particle.getLocation().getX();
        double my = particle.getLocation().getY();
        double ox = other.getLocation().getX();
        double oy = other.getLocation().getY();

        return Point.distanceBetween(new Point(mx,my+l), new Point(ox,oy)) - particle.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx+l,my), new Point(ox,oy)) - particle.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx+l,my+l), new Point(ox,oy)) - particle.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx+l,my-l), new Point(ox,oy)) - particle.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx,my-l), new Point(ox,oy)) - particle.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx-l,my-l), new Point(ox,oy)) - particle.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx-l,my), new Point(ox,oy)) - particle.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx-l,my+l), new Point(ox,oy)) - particle.getRatio() - other.getRatio() <= rc;
    }

    public Map<Particle,Set<Particle>> analyzeCell(Point cell){
        Set<Particle> particles = grid.get(cell);
        Set<Particle> nearCellParticles = getNearMolecules(cell);

        Set<Particle> analyzed = new HashSet<>();
        Map<Particle,Set<Particle>> map = new HashMap<>();

        for(Particle particle : particles){
            Set<Particle> neighbors = getNeighborsOfMolecule(particle,analyzed, nearCellParticles);

            analyzed.add(particle);
            map.put(particle,neighbors);
        }
        return map;
    }

    public double getRc() {
        return rc;
    }
}
