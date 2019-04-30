import ar.edu.itba.ParticleWithMass;

public class DumpledOscillatorSolution {
    private double amplitude;

    public DumpledOscillatorSolution(double amplitude){
        this.amplitude = amplitude;
    }

    public double getPosition(ParticleWithMass p, double gamma, double k, double time){
        double firstTerm = amplitude * Math.exp(-(gamma/(p.getMass() * 2)) * time);
        double secondTerm = Math.cos(Math.pow(k/p.getMass() - (gamma * gamma)/(4*p.getMass()*p.getMass()), 0.5) * time);
        return firstTerm * secondTerm;
    }
}
