package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vue.Observateur;

public class Simulation {
    private int tempsCourant;
    private final Map<String, Entite> entites = new LinkedHashMap<>();
    private final List<Observateur> observateurs = new ArrayList<>();

    public void tick() {
        for (Entite e : new ArrayList<>(entites.values())) {
            e.mettreAJour(this);
        }
        tempsCourant++;
        for (Observateur o : observateurs) {
            o.miseAJour(this);
        }
    }

    public void ajouter(Entite entite) {
        if (entites.containsKey(entite.getId())) {
            throw new IllegalArgumentException("Identifiant deja utilise : " + entite.getId());
        }
        entites.put(entite.getId(), entite);
    }

    public void supprimer(String id) {
        if (entites.remove(id) == null) {
            throw new IllegalArgumentException("Entite inconnue : " + id);
        }
    }

    public void ajouterObservateur(Observateur observateur) {
        observateurs.add(observateur);
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
}
