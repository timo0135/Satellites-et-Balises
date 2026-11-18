package model.satellite;

import model.Entite;
import model.Simulation;


public class Satellite extends Entite {

    public static final double ALTITUDE_PAR_DEFAUT = 200;

    private Orbite orbite;

    public Satellite(String nom, double longitude) {
        this(nom, longitude, ALTITUDE_PAR_DEFAUT, new Geostationnaire());
    }

    public Satellite(String nom, double longitude, double altitude, Orbite orbite) {
        super(nom, longitude, altitude);
        this.orbite = orbite;
    }

    public Orbite getOrbite() {
        return orbite;
    }

    public void setOrbite(Orbite orbite) {
        this.orbite = orbite;
    }

    @Override
    public void mettreAJour(Simulation simulation) {
        orbite.avancer(this);
    }
}
