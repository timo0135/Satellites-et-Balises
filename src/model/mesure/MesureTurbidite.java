package model.mesure;

import model.Position;

/** Turbidite de l'eau, en NTU. */
public class MesureTurbidite extends Mesure {
    private final double turbidite;

    public MesureTurbidite(double instant, Position lieu, double turbidite) {
        super(instant, lieu);
        this.turbidite = turbidite;
    }

    public double getTurbidite() {
        return turbidite;
    }

    @Override
    public String toString() {
        return String.format("Turbidite %.2f NTU (t=%.0f)", turbidite, getInstant());
    }
}
