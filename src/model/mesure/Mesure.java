package model.mesure;

import model.Position;

public abstract class Mesure {
    private final double instant;
    private final Position lieu;

    protected Mesure(double instant, Position lieu) {
        this.instant = instant;
        this.lieu = lieu;
    }

    public double getInstant() {
        return instant;
    }

    public Position getLieu() {
        return lieu;
    }
}
