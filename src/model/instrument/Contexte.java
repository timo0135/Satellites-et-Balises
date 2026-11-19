package model.instrument;

import model.Position;

public class Contexte {
    private final Position position;
    private final double instant;

    public Contexte(Position position, double instant) {
        this.position = position;
        this.instant = instant;
    }

    public Position getPosition() {
        return position;
    }

    public double getInstant() {
        return instant;
    }
}
