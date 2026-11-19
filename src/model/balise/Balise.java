package model.balise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Entite;
import model.Position;
import model.Simulation;
import model.activite.Activite;
import model.activite.Collecte;
import model.deplacement.Deplacement;
import model.evenement.ActiviteChangeeEvent;
import model.evenement.MesureEffectueeEvent;
import model.instrument.Contexte;
import model.instrument.Instrument;
import model.mesure.Mesure;

public class Balise extends Entite {
    private final Memoire memoire;
    private final List<Instrument> instruments = new ArrayList<>();
    private Deplacement deplacementActuel;
    private Activite activite;
    private double instantCourant;

    public Balise(String id, double longitude, double altitude, int capaciteMemoire,
                  Deplacement deplacementActuel) {
        super(id, longitude, altitude);
        this.memoire = new Memoire(capaciteMemoire);
        this.deplacementActuel = deplacementActuel;
        this.activite = new Collecte();
    }

    public void equiper(Instrument instrument) {
        if (instrument == null) {
            throw new IllegalArgumentException("Instrument null");
        }
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

        int avant = memoire.taille();
        activite.agir(this);
        List<Mesure> mesures = memoire.getMesures();
        for (Mesure mesure : new ArrayList<>(mesures.subList(avant, mesures.size()))) {
            simulation.getEventHandler().send(new MesureEffectueeEvent(simulation, this, mesure));
        }

        Activite suivante = activite.suivante(this);
        if (suivante != activite) {
            Activite ancienne = activite;
            changerActivite(suivante);
            simulation.getEventHandler()
                    .send(new ActiviteChangeeEvent(simulation, this, ancienne, suivante));
        }
    }

    public Contexte creerContexte() {
        Position p = getPosition();
        return new Contexte(new Position(p.getLongitude(), p.getAltitude()), instantCourant);
    }

    public Memoire getMemoire() {
        return memoire;
    }

    public List<Instrument> getInstruments() {
        return Collections.unmodifiableList(instruments);
    }

    public Deplacement getDeplacementActuel() {
        return deplacementActuel;
    }

    public Activite getActivite() {
        return activite;
    }
}
