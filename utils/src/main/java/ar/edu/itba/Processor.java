package ar.edu.itba;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Processor {

    private Grid grid;
    private Map<Particle,Set<Particle>> neighbors;
    int m;

    public Processor(int l, int m, double rc, boolean periodic, Set<Particle> particles) {
        this.m = m;
        grid = new Grid(l,m,rc, particles,periodic);
        neighbors = new HashMap<>();
    }

    public Map<Particle,Set<Particle>> start(){
        getNeighborsOfMolecule();
        return neighbors;
    }

    //Algoritmos de simulación.

    public Map<Particle,Set<Particle>> bruteForce(Set<Particle> particles){
        Map<Particle,Set<Particle>> ans = new HashMap<>();
        for(Particle p1: particles){
            ans.put(p1, new HashSet<Particle>());
            for(Particle p2 : particles){
                if(Particle.distanceBetweenParticles(p1,p2)-p1.getRadio()-p2.getRadio() <= grid.getRc()){
                    ans.get(p1).add(p2);
                }
            }
        }
        return ans;
    }

    public void getNeighborsOfMolecule(){
        for(int i = 0; i < m; i++){
            for(int j = 0 ; j < m ; j++){
                Map<Particle,Set<Particle>> map = grid.processCell(new Point(i,j));
                for(Particle particle : map.keySet()) {
                    if (neighbors.containsKey(particle)) {
                        neighbors.get(particle).addAll(map.get(particle));
                    } else {
                        neighbors.put(particle, map.get(particle));
                    }
                    for (Particle m : map.get(particle)) {
                        if (neighbors.containsKey(m)) {
                            neighbors.get(m).add(particle);
                        } else {
                            HashSet<Particle> set = new HashSet<>();
                            set.add(particle);
                            neighbors.put(m, set);
                        }
                    }
                }
            }
        }

    }

    public static void writeToFile(String data, int index, String path){
        try {
            Files.write(Paths.get(path + "/simul" + index + ".xyz"), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
