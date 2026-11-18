package vue;

import java.awt.Point;

import model.Position;

/**
 * Passage des coordonnees du monde (longitude, altitude) aux pixels de la
 * vue de cote.
 * <p>
 * Le monde est circulaire (c'est le tour de la Terre) : une longitude hors de
 * [0, largeurMonde[ reapparait de l'autre cote. L'altitude croit vers le haut
 * de l'ecran.
 */
public class Repere {
    private final double largeurMonde;
    private final double altitudeMin;
    private final double altitudeMax;
    private final int largeurPx;
    private final int hauteurPx;

    public Repere(double largeurMonde, double altitudeMin, double altitudeMax,
                  int largeurPx, int hauteurPx) {
        if (largeurMonde <= 0 || altitudeMin >= altitudeMax) {
            throw new IllegalArgumentException("Dimensions du monde invalides");
        }
        this.largeurMonde = largeurMonde;
        this.altitudeMin = altitudeMin;
        this.altitudeMax = altitudeMax;
        this.largeurPx = largeurPx;
        this.hauteurPx = hauteurPx;
    }

    public Point versEcran(Position position) {
        double longitude = position.getLongitude() % largeurMonde;
        if (longitude < 0) {
            longitude += largeurMonde;
        }
        int x = (int) Math.round(longitude / largeurMonde * largeurPx);
        return new Point(x, ordonnee(position.getAltitude()));
    }

    /** Ordonnee en pixels d'une altitude donnee. */
    public int ordonnee(double altitude) {
        return (int) Math.round((altitudeMax - altitude) / (altitudeMax - altitudeMin) * hauteurPx);
    }

    public double getLargeurMonde() {
        return largeurMonde;
    }

    public int getLargeurPx() {
        return largeurPx;
    }

    public int getHauteurPx() {
        return hauteurPx;
    }
}
