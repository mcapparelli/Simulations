import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Grid {
    private int l;
    private int m;
    private double rc;
    private double fieldSize;
    private HashMap<Vector, Set<Particle>> grid;
    private boolean periodic;

    public Grid(int l, int n, int m, double rc, Set<Particle> molecules, boolean periodic) {
        this.l = l;
        this.m = m;
        this.rc = rc;
        this.fieldSize = (double)l/m;
        this.grid = new HashMap<>();
        this.periodic = periodic;

        for(int i = 0; i < m ; i++){
            for(int j = 0 ; j < m ; j++) {
                grid.put(new Vector(i, j), new HashSet<Particle>());
            }
        }

        for(Particle molecule : molecules){
            Vector j = getField(molecule.getPosition());
            try {
                grid.get(j).add(molecule);
            }catch (Exception e){
                System.out.println(j + " " +molecule);
            }

        }

    }

    private Vector getField(Vector location){
        int x = (int)(location.getX()/fieldSize);
        int y = (int)(location.getY()/fieldSize);
        return new Vector(x,y);
    }

    public Set<Particle> getMoleculesInCell(int x , int y){
        if(x < 0){
            if(periodic){
                return getMoleculesInCell(x+m,y);
            }else{
                return Collections.EMPTY_SET;
            }

        } if(x >= m){
            if(periodic){
                return getMoleculesInCell(x-m,y);
            }else{
                return Collections.EMPTY_SET;
            }
        }
        if (y <0){
            if(periodic){
                return getMoleculesInCell(x,y+m);
            }else{
                return Collections.EMPTY_SET;
            }
        }
        if( y>=m){
            if(periodic){
                return getMoleculesInCell(x,y-m);
            }else{
                return Collections.EMPTY_SET;
            }

        }
        return grid.get(new Vector(x,y));
    }

    private Set<Particle> getNearMolecules(Vector field){
        Set<Particle> nearMolecules = new HashSet<>();
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()      ,   (int) field.getY()));
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()+1 ,   (int) field.getY()));
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()+1 ,(int) field.getY()+1));
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()      ,(int) field.getY()+1));
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()+1 ,(int) field.getY()-1));
        return nearMolecules;
    }

    public Set<Particle> getNeighborsOfMolecule(Particle molecule, Set<Particle> analyzed, Set<Particle> nearMolecules){
        final Vector filed = getField(molecule.getPosition());
        //Set<Molecule> nearMolecules = getNearMolecules(filed);
        //nearMolecules.remove(molecule);
        //nearMolecules.removeAll(analyzed);
        Set<Particle> ans = new HashSet<>();

        for(Particle other : nearMolecules){
            if(!other.equals(molecule) && !analyzed.contains(other)) {
                if(calculateDistanceBetweenMolecules(molecule,other)){
                    ans.add(other);
                }
            }
        }

        return ans;
    }

    private boolean calculateDistanceBetweenMolecules(Particle molecule, Particle other) {

//        if(Particle.distanceBetweenMolecules(molecule,other)-molecule.getRadius()-other.getRadius()<=rc) {
//            return true;
//        }
        if(molecule.getPosition().subtract(other.getPosition()).abs()-molecule.getRadius()-other.getRadius()<=rc) {
            return true;
        }
        if(!periodic){
            return false;
        }

//        double mx = molecule.getLocation().getX();
//        double my = molecule.getLocation().getY();
//        double ox = other.getLocation().getX();
//        double oy = other.getLocation().getY();
//
//        return Point.distanceBetween(new Point(mx,my+l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
//        || Point.distanceBetween(new Point(mx+l,my), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
//        || Point.distanceBetween(new Point(mx+l,my+l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
//        || Point.distanceBetween(new Point(mx+l,my-l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
//        || Point.distanceBetween(new Point(mx,my-l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
//        || Point.distanceBetween(new Point(mx-l,my-l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
//        || Point.distanceBetween(new Point(mx-l,my), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
//        || Point.distanceBetween(new Point(mx-l,my+l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc;
        return false;
    }

    public Map<Particle,Set<Particle>> analyzeCell(Vector cell){
        Set<Particle> molecules = grid.get(cell);
        Set<Particle> nearCellMolecules = getNearMolecules(cell);

        Set<Particle> analyzed = new HashSet<>();
        Map<Particle,Set<Particle>> map = new HashMap<>();

        for(Particle molecule : molecules){
            Set<Particle> neighbors = getNeighborsOfMolecule(molecule,analyzed, nearCellMolecules);

            analyzed.add(molecule);
            map.put(molecule,neighbors);
        }
        return map;
    }

    public double getRc() {
        return rc;
    }
}
