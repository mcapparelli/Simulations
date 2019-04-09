import ar.edu.itba.ParticleWithMass;

import java.util.Set;

public abstract class  Collision implements Comparable<Collision> {

    private double time;

    public Collision(double time){
     this.time=time;
    }

    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Collision o){
        return Double.compare(this.getTime(),o.getTime());
    }

    public abstract Set<ParticleWithMass> getParticles();

    public abstract boolean contains(ParticleWithMass p);
}
