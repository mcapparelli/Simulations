package ar.edu.itba;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class IO {
    private BufferedReader staticBuffer;
    private BufferedReader dynamicBuffer;
    private int n;
    private int l;
    private int m;
    private double rc;
    private double radio;
    private List<Particle> particles;
    private int time;
    private boolean periodic;

    public IO(String stat){
        File staticFile = new File(stat);
        particles = new ArrayList<>();
        try {
            staticBuffer = new BufferedReader(new FileReader(staticFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            time = 0;
            n = Integer.parseInt(staticBuffer.readLine());
            l = Integer.parseInt(staticBuffer.readLine());
            m = Integer.parseInt(staticBuffer.readLine());
            rc = Double.parseDouble(staticBuffer.readLine());
            periodic = staticBuffer.readLine().equals("periodic");
            radio = Double.parseDouble(staticBuffer.readLine());
            for (int i = 0; i < n; i++){
                particles.add(new Particle(radio,null,0,0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getN() {
        return n;
    }

    public int getL() { return l; }

    public int getM() {
        return m;
    }

    public double getRc() {
        return rc;
    }

    public Set<Particle> getCleanParticles(){
        return new HashSet<>(particles);
    }

    public Set<Particle> getParticles() {
        Set<Particle> ans = new HashSet<>();
        try{
            if (!dynamicBuffer.readLine().equals("t"+time)){
                throw new IllegalArgumentException();
            }
            for(int i =0; i<n ; i++){
                String line = dynamicBuffer.readLine();
                String[] data = line.split(" ");
                Particle current = particles.get(i);
                double x,y,v,angle;
                x=Double.parseDouble(data[0]);
                y=Double.parseDouble(data[1]);
                v=Double.parseDouble(data[2]);
                angle=Double.parseDouble(data[3]);
                ans.add(new Particle(current.getId(),current.getRadio(),null, new Point(x,y),v,angle));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return ans;
    }

    public boolean isPeriodic() {
        return periodic;
    }

    private static String RGBtoDouble(double radius){
        while(radius<0){
            radius+=Math.PI*2;
        }
        double r,g,b;
        if(radius < Math.PI/3){
            r=1;
            g=(radius/(Math.PI/3));
            b=0;
        }else if(radius < Math.PI*2/3){
            r=1-((radius - Math.PI/3) / (Math.PI/3));
            g=1;
            b=0;
        }else if(radius < Math.PI){
            r=0;
            g=1;
            b=((radius - 2 * Math.PI/3) / (Math.PI/3));

        }else if(radius < Math.PI * 4/3){
            r=0;
            g=1-((radius - Math.PI) / (Math.PI/3));
            b=255;
        }else if(radius < Math.PI * 5/3){
            r=((radius - 4 * Math.PI/3) / (Math.PI/3));
            g=0;
            b=255;
        }else if(radius <= Math.PI*2){
            r=255;
            g=0;
            b=1 - ((radius - 5 * Math.PI/3) / (Math.PI/3));
        }else {
            r=1;
            g=1;
            b=1;
        }
        return r + " " + g + " " + b + " ";
    }


    public static StringBuilder generateFileOffLattice(Set<Particle> particles, StringBuilder builder){
        builder.append(particles.size())
                .append("\r\n")
                .append("id\tX\tY\tRad\tR\tG\tB\tVx\tVy\t\r\n");
        for(Particle current: particles){
            double vx = current.getVelocity()*Math.cos(current.getAngle());
            double vy = current.getVelocity()*Math.sin(current.getAngle());
            builder.append(current.getId())
                    .append(" ")
                    .append(current.getLocation().getX())
                    .append(" ")
                    .append(current.getLocation().getY())
                    .append(" ")
                    .append(current.getRadio())
                    .append(" ")
                    .append(RGBtoDouble(current.getAngle()))
                    .append(vx)
                    .append(" ")
                    .append(vy)
                    .append(" ")
                    .append(current.getAngle())
                    .append("\r\n");
        }
        return builder;
    }

    public static StringBuilder generateFileOscillator(StringBuilder builder, List<Result> results){
        builder.append(1)
                .append("\r\n")
                .append("DeltaT\tXSol\tXLeap\tXBee\tXGear\t\r\n");
        for(Result current: results){
            builder.append(current.getTime())
                    .append("\t")
                    .append(current.getPositionSolution())
                    .append("\t")
                    .append(current.getPositionLeapFrog())
                    .append("\t")
                    .append(current.getPositionBeeman())
                    .append("\t")
                    .append(current.getPositionGear())
                    .append("\r\n");
        }
        return builder;
    }

    public static StringBuilder generateFileTimeVa(int time, double va, StringBuilder builder){
        builder.append("\r\n")
               .append("T\tVa\r\n");
        return builder;
    }

    public static String generateFileString(Particle particle, Set<Particle> neighbours, Set<Particle> allParticles){
        StringBuilder builder = new StringBuilder()
                .append(allParticles.size())
                .append("\r\n")
                .append("id\tX\tY\trad\tR\tG\tB\t \r\n");
        for(Particle current: allParticles){
            builder.append(current.getId())
                    .append(" ")
                    .append(current.getLocation().getX())
                    .append(" ")
                    .append(current.getLocation().getY())
                    .append(" ")
                    .append(current.getRadio())
                    .append(" ");
            if(particle.getId() == current.getId()){
                builder.append("1 0 0\r\n");
            }else if(neighbours.contains(current)){
                builder.append("0 1 0\r\n");
            }else{
                builder.append("1 1 1\r\n");
            }
        }
        return builder.toString();
    }

    public IO(String s, String d) {
        File staticFile = new File(s);
        File dinamicFile = new File(d);
        time=0;
        particles = new ArrayList<>();
        try {
            staticBuffer = new BufferedReader(new FileReader(staticFile));
            dynamicBuffer = new BufferedReader(new FileReader(dinamicFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            n = Integer.parseInt(staticBuffer.readLine());
            l = Integer.parseInt(staticBuffer.readLine());
            m = Integer.parseInt(staticBuffer.readLine());
            rc = Double.parseDouble(staticBuffer.readLine());
            periodic = staticBuffer.readLine().equals("periodic");
            for (int i = 0; i < n; i++){
                String[] array = staticBuffer.readLine().split(" ");
                particles.add(new Particle(Double.parseDouble(array[0]), new Property<String>("property"),null,0,0));
            }
            staticBuffer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        //System.out.println("N:"+n+" L:"+l+" m:"+m+" rc:"+rc);
    }


    public static void writeToFileOffLatticeXYZ(String name, String data, String path){
        try {
            Files.write(Paths.get(path + "/" + name + ".xyz"), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFileOscillator(String name, String data, String path){
        try {
            Files.write(Paths.get(path + "/" + name + ".txt"), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFileOffLatticeTXT(String name, String data, String path){
        try {
            Files.write(Paths.get(path + "/" + name + ".txt"), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}