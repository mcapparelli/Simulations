package models;
import javafx.util.Pair;
import models.Cell;
import models.Buckets;
import models.Particle;

import java.util.*;
import java.util.stream.Collectors;

public class NeighborS {

    public static Map<Particle, List<Particle>> getNeighbors(Buckets buckets, HashSet<Pair<Integer, Integer>> usedCells, Double interactionRadio, Boolean contornCondition){
        Map<Particle, List<Particle>> result = new HashMap<>();
        usedCells.forEach(pair -> {
            int i = pair.getKey(), j = pair.getValue();
            for (Particle current : buckets.getCell(i, j).getParticles()){
                List<Particle> currentNeighbors = new ArrayList<>();
                List<Particle> sameCell = new ArrayList<>();

                final List<Particle> neighbors = result.getOrDefault(current, new LinkedList<>());

                if (!contornCondition) {
                    if (i != 0)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                buckets.getCell(i - 1, j), interactionRadio, contornCondition, buckets.getSideLength()));

                    if (i != 0 && j != buckets.getSideCellsQuantity() - 1)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                buckets.getCell(i - 1, j + 1), interactionRadio, contornCondition, buckets.getSideLength()));

                    if (j != buckets.getSideCellsQuantity() - 1)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                buckets.getCell(i, j + 1), interactionRadio, contornCondition, buckets.getSideLength()));

                    if (j != buckets.getSideCellsQuantity() - 1 && i != buckets.getSideCellsQuantity() - 1)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                buckets.getCell(i + 1, j + 1), interactionRadio, contornCondition, buckets.getSideLength()));

                    }else {
                        currentNeighbors.addAll(getNeighborParticles(current,
                                buckets.getSideCell((i - 1)  , j), interactionRadio, contornCondition, buckets.getSideLength()));

                        currentNeighbors.addAll(getNeighborParticles(current,
                                buckets.getSideCell(i - 1, j + 1), interactionRadio, contornCondition, buckets.getSideLength()));

                        currentNeighbors.addAll(getNeighborParticles(current,
                                buckets.getSideCell(i, j + 1), interactionRadio, contornCondition, buckets.getSideLength()));

                        currentNeighbors.addAll(getNeighborParticles(current,
                                buckets.getSideCell(i + 1, j + 1), interactionRadio, contornCondition, buckets.getSideLength()));
                }

                sameCell.addAll(getNeighborParticles(current,
                        buckets.getCell(i, j), interactionRadio, contornCondition, buckets.getSideLength()));

                neighbors.addAll(currentNeighbors);
                neighbors.addAll(sameCell);

                for (Particle newRelation : currentNeighbors) {
                    final List<Particle> anotherNeighbors = result.getOrDefault(newRelation, new LinkedList<>());
                    anotherNeighbors.add(current);
                    result.put(newRelation, anotherNeighbors);
                }

                result.put(current, neighbors);
            }
        });
        return result;
    }


    private static List<Particle> getNeighborParticles(Particle current, Cell cell, Double interactionRadio, boolean contorn, int gridSize){
        return cell.getParticles().stream()
                .parallel()
                .filter(another -> (getDistance(current, another, contorn, gridSize)) <= interactionRadio)
                .filter(another -> !current.equals(another))
                .collect(Collectors.toList());
    }

    private static Double getDistance(Particle p1, Particle p2, boolean contorn, int size){
        double y = Math.abs(p2.getY() - p1.getY());
        double x = Math.abs(p2.getX() - p1.getX());
        double h = Math.hypot(y, x);
        if (contorn){
            double xc = Math.abs(p1.getX() - p2.getX());
            xc = Math.min(xc, size - xc);
            double yc = (double)size - Math.abs(p1.getY() - p2.getY());
            yc = Math.min(yc, size - yc);
            return Math.min(h, Math.hypot(xc, yc));
        }
        return h;
    }

    public static Double getForce(Particle particle, List<Particle> neighbors){
        return null;
    }
}
