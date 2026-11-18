package model.deplacement;

import model.Position;
import model.balise.Balise;


public class Sinusoidal implements Deplacement {
    private final double vitesseX;
    private final double amplitude;
    private final double periode; // en pas de temps
    private final double altitudeCentre;
    private int pas;

    public Sinusoidal(double vitesseX, double amplitude, double periode, double altitudeCentre) {
        if (periode <= 0) {
            throw new IllegalArgumentException("La periode doit etre positive");
        }
        this.vitesseX = vitesseX;
        this.amplitude = amplitude;
        this.periode = periode;
        this.altitudeCentre = altitudeCentre;
    }

    @Override
    public void deplacer(Balise balise) {
        pas++;
        Position p = balise.getPosition();
        p.setLongitude(p.getLongitude() + vitesseX);
        double altitude = altitudeCentre + amplitude * Math.sin(2 * Math.PI * pas / periode);
        p.setAltitude(Math.min(0, altitude)); // jamais au-dessus de la surface
    }

    public double getVitesseX() {
        return vitesseX;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public double getPeriode() {
        return periode;
    }

    public double getAltitudeCentre() {
        return altitudeCentre;
    }

    public int getPas() {
        return pas;
    }
}
