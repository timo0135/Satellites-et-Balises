package model.deplacement;

import model.Position;
import model.balise.Balise;

/**
 * Rejoint une profondeur donnee (profondeur &lt;= 0) a vitesse verticale
 * constante, puis s'y maintient.
 */
public class ProfondeurFixe implements Deplacement {
    private final double profondeur;
    private final double vitesse;

    public ProfondeurFixe(double profondeur, double vitesse) {
        if (profondeur > 0) {
            throw new IllegalArgumentException("La profondeur doit etre <= 0");
        }
        if (vitesse <= 0) {
            throw new IllegalArgumentException("La vitesse doit etre positive");
        }
        this.profondeur = profondeur;
        this.vitesse = vitesse;
    }

    @Override
    public void deplacer(Balise balise) {
        Position p = balise.getPosition();
        double altitude = p.getAltitude();
        if (altitude > profondeur) {
            p.setAltitude(Math.max(profondeur, altitude - vitesse));
        } else if (altitude < profondeur) {
            p.setAltitude(Math.min(profondeur, altitude + vitesse));
        }
    }

    public double getProfondeur() {
        return profondeur;
    }

    public double getVitesse() {
        return vitesse;
    }
}
