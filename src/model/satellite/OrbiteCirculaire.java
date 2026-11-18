package model.satellite;

public class OrbiteCirculaire implements Orbite {
    private final double circonference;
    private final double vitesse; // longitude parcourue par pas de temps, > 0

    public OrbiteCirculaire(double circonference, double vitesse) {
        if (circonference <= 0) {
            throw new IllegalArgumentException("La circonference doit etre positive");
        }
        if (vitesse <= 0) {
            throw new IllegalArgumentException("La vitesse doit etre positive (sens unique)");
        }
        this.circonference = circonference;
        this.vitesse = vitesse;
    }

    @Override
    public void avancer(Satellite satellite) {
        double longitude = (satellite.getPosition().getLongitude() + vitesse) % circonference;
//        if (longitude < 0) {
//            longitude += circonference;
//        }
        satellite.getPosition().setLongitude(longitude);
    }

    public double getCirconference() {
        return circonference;
    }

    public double getVitesse() {
        return vitesse;
    }
}
