package model.mesure;

import model.Position;

public class MesureCourant extends Mesure {
    private final double vitesse;
    private final double direction;

    public MesureCourant(double instant, Position lieu, double vitesse, double direction) {
        super(instant, lieu);
        this.vitesse = vitesse;
        this.direction = direction;
    }

    public double getVitesse() {
        return vitesse;
    }

    public double getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return String.format("Courant %.2f m/s, %.0f deg (t=%.0f)", vitesse, direction, getInstant());
    }
}
