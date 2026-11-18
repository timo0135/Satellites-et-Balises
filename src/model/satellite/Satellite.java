package model.satellite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Entite;
import model.Simulation;
import model.mesure.Mesure;

public class Satellite extends Entite {

    public static final double ALTITUDE_PAR_DEFAUT = 1000;

    private boolean disponible;
    private Orbite orbite;
    private final List<Mesure> donneesRecues = new ArrayList<>();

    public Satellite(String nom, double longitude) {
        this(nom, longitude, ALTITUDE_PAR_DEFAUT, new Geostationnaire());
    }

    public Satellite(String nom, double longitude, double altitude, Orbite orbite) {
        super(nom, longitude, altitude);
        this.disponible = true;
        this.orbite = orbite;
    }

    public boolean estDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Orbite getOrbite() {
        return orbite;
    }

    public void setOrbite(Orbite orbite) {
        this.orbite = orbite;
    }

    /** Conserve les mesures transmises par une balise. */
    public void recevoir(List<Mesure> mesures) {
        donneesRecues.addAll(mesures);
    }

    public List<Mesure> getDonneesRecues() {
        return Collections.unmodifiableList(donneesRecues);
    }

    @Override
    public void mettreAJour(Simulation simulation) {
        orbite.avancer(this);
    }
}
