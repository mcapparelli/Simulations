package models;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static javafx.application.Platform.exit;

public class Buckets {
    private Cell[][] cells;
    private HashSet<Pair<Integer, Integer>> usedCells;
    private int sideCellsQuantity;
    private int sideLength;

    public Buckets(int sideCellsQuantity, int sideLength) {
        this.sideCellsQuantity = sideCellsQuantity;
        this.cells = new Cell[sideCellsQuantity][sideCellsQuantity];
        for (int i = 0 ; i < sideCellsQuantity ; i++)
            for (int j = 0 ; j < sideCellsQuantity ; j++)
                this.cells[i][j] = new Cell();
        this.sideLength = sideLength;
    }

    public Cell getCell(int x, int y){
        return cells[x][y];
    }

    public Cell getSideCell(int x, int y){
        return cells[Math.floorMod(x, sideCellsQuantity)][Math.floorMod(y, sideCellsQuantity)];
        }


    public void setCell(int x, int y, Cell cell){
        cells[x][y] = cell;
    }

    public int getSideCellsQuantity() {
        return sideCellsQuantity;
    }

    public int getSideLength() {
        return sideLength;
    }

    public void setParticles(List<Particle> particles){
        usedCells = new HashSet<>();
        Double cellSideLength = Double.valueOf(sideLength) / Double.valueOf(sideCellsQuantity);
        for (Particle particle : particles){
            int row = (int)Math.floor(particle.getY() / cellSideLength); // Cast truncates
            int column = (int)Math.floor(particle.getX() / cellSideLength); // Cast truncates
            try {
                cells[row][column].addParticle(particle);
                usedCells.add(new Pair(row, column));
            } catch (Exception e){
                System.out.println("Wrong dt.");
                System.exit(0);
            }
        }
    }

    public HashSet<Pair<Integer, Integer>> getUsedCells() {
        return usedCells;
    }
}
