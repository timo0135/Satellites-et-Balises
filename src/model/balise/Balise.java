package model.balise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Entite;
import model.Simulation;
import model.activite.Activite;
import model.activite.Collecte;
import model.activite.Transmission;
import model.deplacement.Deplacement;
import model.instrument.Contexte;
import model.instrument.Instrument;

public class Balise extends Entite {
    private final Memoire memoire;
    private final List<Instrument> instruments = new ArrayList<>();
    private Deplacement deplacementActuel;
    private Activite activite;
    private double instantCourant;

    public Balise(String id, double longitude, int capaciteMemoire, Deplacement deplacementActuel) {
        this(id, longitude, 0, capaciteMemoire, deplacementActuel);
    }

    public Balise(String id, double longitude, double altitude, int capaciteMemoire,
                  Deplacement deplacementActuel) {
        super(id, longitude, altitude);
        this.memoire = new Memoire(capaciteMemoire);
        this.deplacementActuel = deplacementActuel;
        this.activite = new Collecte();
    }

    public void equiper(Instrument instrument) {
        instruments.add(instrument);
    }

    public void desequiper(Instrument instrument) {
        instruments.remove(instrument);
    }

    public void changerDeplacement(Deplacement deplacement) {
        if (deplacement == null) {
            throw new IllegalArgumentException("Deplacement null");
        }
        this.deplacementActuel = deplacement;
    }

    public void changerActivite(Activite activite) {
        if (activite == null) {
            throw new IllegalArgumentException("Activite null");
        }
        this.activite = activite;
    }

    @Override
    public void mettreAJour(Simulation simulation) {
        instantCourant = simulation.getTempsCourant();
        activite.agir(this);
        Activite suivante = activite.suivante(this);
        if (suivante != activite) {
            changerActivite(suivante);
        }
    }

    public Contexte creerContexte() {
        return new Contexte(getPosition().copie(), instantCourant);
    }

    public boolean enAttenteDeTransmission() {
        return activite instanceof Transmission && !memoire.estVide();
        // a refaire
    }

    public Memoire getMemoire() {
        return memoire;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public Deplacement getDeplacementActuel() {
        return deplacementActuel;
    }

    public Activite getActivite() {
        return activite;
    }
}
