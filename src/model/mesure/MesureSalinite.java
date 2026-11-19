package model.mesure;

import model.Position;

public class MesureSalinite extends Mesure {
    private final double salinite;

    public MesureSalinite(double instant, Position lieu, double salinite) {
        super(instant, lieu);
        this.salinite = salinite;
    }

    public double getSalinite() {
        return salinite;
    }

    @Override
    public String toString() {
        return String.format("Salinite %.2f g/kg (t=%.0f)", salinite, getInstant());
    }
}
