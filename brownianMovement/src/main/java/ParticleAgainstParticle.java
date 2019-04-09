import ar.edu.itba.ParticleWithMass;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParticleAgainstParticle extends Collision {
    ParticleWithMass p1;
    ParticleWithMass p2;

    public ParticleAgainstParticle(ParticleWithMass p1, ParticleWithMass p2, double time){
        super(time);
        this.p1 = p1;
        this.p2 = p2;

    }

    @Override
    public String toString() {
        return "ParticleAgainstParticle{" +
                "p1=" + p1.getId() +
                ", p2=" + p2.getId() +
                ", time=" + this.getTime() +
                '}';
    }

    @Override
    public Set<ParticleWithMass> getParticles() {
        return Stream.of(p1,p2).collect(Collectors.toSet());
    }

    @Override
    public boolean contains(ParticleWithMass p) {
        return p1.equals(p) || p2.equals(p);
    }
}
