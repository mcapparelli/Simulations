package models;

public class GPredictor {
    private Point r;
    private Point r1;
    private Point r2;
    private Point r3;
    private Point r4;
    private Point r5;

    public GPredictor(Point r, Point r1, Point r2, Point r3, Point r4, Point r5) {
        this.r = r;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
    }

    public GPredictor(){
        this.r = new Point();
        this.r1 = new Point();
        this.r2 = new Point();
        this.r3 = new Point();
        this.r4 = new Point();
        this.r5 = new Point();
    }

    public GPredictor(Double x, Double y, Double vx, Double vy){
        this.r = new Point(x,y);
        this.r1 = new Point(vx,vy);
        this.r2 = new Point();
        this.r3 = new Point();
        this.r4 = new Point();
        this.r5 = new Point();
    }


    public Point getR() {
        return r;
    }

    public Point getR1() {
        return r1;
    }

    public Point getR2() {
        return r2;
    }

    public Point getR3() {
        return r3;
    }

    public Point getR4() {
        return r4;
    }

    public Point getR5() {
        return r5;
    }
}
