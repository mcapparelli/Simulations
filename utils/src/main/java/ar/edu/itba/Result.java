package ar.edu.itba;

public class Result {
    private double positionSolution;
    private double positionLeapFrog;
    private double positionBeeman;
    private double positionGear;
    private double time;

    public Result(double sol, double leap, double bee, double gear, double time){
        this.positionSolution = sol;
        this.positionLeapFrog = leap;
        this.positionBeeman = bee;
        this.positionGear = gear;
        this.time = time;
    }

    public double getPositionSolution() {
        return positionSolution;
    }

    public void setPositionSolution(double positionSolution) {
        this.positionSolution = positionSolution;
    }

    public double getPositionLeapFrog() {
        return positionLeapFrog;
    }

    public void setPositionLeapFrog(double positionLeapFrog) {
        this.positionLeapFrog = positionLeapFrog;
    }

    public double getPositionBeeman() {
        return positionBeeman;
    }

    public void setPositionBeeman(double positionBeeman) {
        this.positionBeeman = positionBeeman;
    }

    public double getPositionGear() {
        return positionGear;
    }

    public void setPositionGear(double positionGear) {
        this.positionGear = positionGear;
    }

    public double getTime() {
        return time;
    }
}
