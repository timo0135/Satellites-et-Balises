package model.deplacement;

import model.Position;
import model.balise.Balise;

public class Horizontal implements Deplacement {
    private final double dx;

    public Horizontal(double dx) {
        this.dx = dx;
    }

    @Override
    public void deplacer(Balise balise) {
        Position p = balise.getPosition();
        p.setLongitude(p.getLongitude() + dx);
    }

    public double getDx() {
        return dx;
    }
}
