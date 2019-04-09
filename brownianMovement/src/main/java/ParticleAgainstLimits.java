import ar.edu.itba.ParticleWithMass;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParticleAgainstLimits extends Collision {
    ParticleWithMass p1;

    public ParticleAgainstLimits(ParticleWithMass p, double time){
        super(time);
        this.p1=p;
    }

    @Override
    public String toString() {
        return "WallCollsion{" +
                "p1=" + p1.getId() +
                ", time=" + this.getTime() +
                '}';
    }

    @Override
    public Set<ParticleWithMass> getParticles() {
        return Stream.of(p1).collect(Collectors.toSet());
    }

    @Override
    public boolean contains(ParticleWithMass p) {
        return p1.equals(p);
    }
}
