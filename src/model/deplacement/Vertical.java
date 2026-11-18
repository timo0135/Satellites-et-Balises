package model.deplacement;

import model.Position;
import model.balise.Balise;


public class Vertical implements Deplacement {
    private final double vitesse;
    private final double altitudeMin;
    private final double altitudeMax;
    private int sens = -1; // -1 : descend, +1 : remonte

    public Vertical(double vitesse, double altitudeMin, double altitudeMax) {
        if (vitesse <= 0) {
            throw new IllegalArgumentException("La vitesse doit etre positive");
        }
        if (altitudeMin >= altitudeMax || altitudeMax > 0) {
            throw new IllegalArgumentException("Il faut altitudeMin < altitudeMax <= 0");
        }
        this.vitesse = vitesse;
        this.altitudeMin = altitudeMin;
        this.altitudeMax = altitudeMax;
    }

    @Override
    public void deplacer(Balise balise) {
        Position p = balise.getPosition();
        double altitude = p.getAltitude() + sens * vitesse;
        if (altitude <= altitudeMin) {
            altitude = altitudeMin;
            sens = 1;
        } else if (altitude >= altitudeMax) {
            altitude = altitudeMax;
            sens = -1;
        }
        p.setAltitude(altitude);
    }

    public double getVitesse() {
        return vitesse;
    }

    public double getAltitudeMin() {
        return altitudeMin;
    }

    public double getAltitudeMax() {
        return altitudeMax;
    }

    public int getSens() {
        return sens;
    }
}
