package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.balise.Balise;
import model.communication.GestionnaireCommunication;
import model.communication.Transfert;
import model.satellite.Satellite;
import vue.Observateur;


public class Simulation {
    private int tempsCourant;
    private final Map<String, Entite> entites = new LinkedHashMap<>();
    private final List<Transfert> transferts = new ArrayList<>();
    private final List<Observateur> observateurs = new ArrayList<>();
    private final GestionnaireCommunication gestionnaire;

    public Simulation() {
        this(new GestionnaireCommunication());
    }

    public Simulation(GestionnaireCommunication gestionnaire) {
        this.gestionnaire = gestionnaire;
    }


    public void tick() {
        for (Entite e : new ArrayList<>(entites.values())) {
            e.mettreAJour(this);
        }
        gestionnaire.detecter(this);
        for (Transfert t : new ArrayList<>(transferts)) {
            t.avancer();
            if (t.estTermine()) {
                transferts.remove(t);
            }
        }
        tempsCourant++;
        for (Observateur o : observateurs) {
            o.miseAJour(this);
        }
    }

    /** @throws IllegalArgumentException si l'identifiant est deja utilise */
    public void ajouter(Entite entite) {
        if (entites.containsKey(entite.getId())) {
            throw new IllegalArgumentException("Identifiant deja utilise : " + entite.getId());
        }
        entites.put(entite.getId(), entite);
    }

    /**
     * Supprime l'entite et interrompt les transferts qui la concernent.
     *
     * @throws IllegalArgumentException si l'identifiant est inconnu
     */
    public void supprimer(String id) {
        Entite entite = entites.remove(id);
        if (entite == null) {
            throw new IllegalArgumentException("Entite inconnue : " + id);
        }
        for (Transfert t : new ArrayList<>(transferts)) {
            if (t.getBalise() == entite || t.getSatellite() == entite) {
                t.interrompre();
                transferts.remove(t);
            }
        }
    }

    public void ajouterObservateur(Observateur observateur) {
        observateurs.add(observateur);
    }

    public void ajouterTransfert(Transfert transfert) {
        transferts.add(transfert);
    }

    public void retirerTransfert(Transfert transfert) {
        transferts.remove(transfert);
    }

    public boolean aUnTransfert(Balise balise) {
        for (Transfert t : transferts) {
            if (t.getBalise() == balise) {
                return true;
            }
        }
        return false;
    }

    public int getTempsCourant() {
        return tempsCourant;
    }

    public Entite getEntite(String id) {
        return entites.get(id);
    }

    public Collection<Entite> getEntites() {
        return Collections.unmodifiableCollection(entites.values());
    }

    public List<Balise> getBalises() {
        List<Balise> balises = new ArrayList<>();
        for (Entite e : entites.values()) {
            if (e instanceof Balise) {
                balises.add((Balise) e);
            }
        }
        return balises;
    }

    public List<Satellite> getSatellites() {
        List<Satellite> satellites = new ArrayList<>();
        for (Entite e : entites.values()) {
            if (e instanceof Satellite) {
                satellites.add((Satellite) e);
            }
        }
        return satellites;
    }

    public List<Transfert> getTransferts() {
        return Collections.unmodifiableList(transferts);
    }

    public GestionnaireCommunication getGestionnaire() {
        return gestionnaire;
    }
}
