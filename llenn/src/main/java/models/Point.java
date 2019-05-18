package models;

public class Point {
    private Double x;
    private Double y;

    public Point(){
        this.x = 0.0;
        this.y = 0.0;
    }

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
    public Point(Point base){
        this.x = base.x;
        this.y = base.y;
    }


    public Point add(Double magnitude){
        return new Point(this.x + magnitude, this.y + magnitude);
    }

    public Point multiply(Double magnitude){
        return new Point(this.x * magnitude, this.y * magnitude);
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Point add(Point vector2D){
        return new Point(this.x + vector2D.x, this.y + vector2D.y);
    }
}
