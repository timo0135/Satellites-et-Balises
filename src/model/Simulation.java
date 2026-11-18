package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import eventHandler.EventHandler;
import model.evenement.EntiteAjouteeEvent;
import model.evenement.EntiteDeplaceeEvent;
import model.evenement.EntiteSupprimeeEvent;
import model.evenement.PasDeTempsEvent;

/**
 * Le coeur de la simulation (v1) : contient les entites (satellites et
 * balises) et fait avancer le temps par pas discrets. Un appel a
 * {@link #tick()} = un pas : chaque entite se met a jour.
 * <p>
 * Elle ne connait pas la vue : elle envoie des evenements (entite ajoutee,
 * supprimee, deplacee, pas de temps ecoule) a ceux qui se sont enregistres
 * aupres de son {@link EventHandler}, comme dans le TP Account :
 * <pre>
 * simulation.getEventHandler().registerListener(PasDeTempsEvent.class, ecouteur);
 * </pre>
 */
public class Simulation {
    private int tempsCourant;
    private final Map<String, Entite> entites = new LinkedHashMap<>();
    private final EventHandler eventHandler = new EventHandler();

    /**
     * Un pas de temps : chaque entite se met a jour (et signale son
     * deplacement s'il y a lieu), puis la fin du pas est annoncee.
     */
    public void tick() {
        for (Entite e : new ArrayList<>(entites.values())) {
            double longitude = e.getPosition().getLongitude();
            double altitude = e.getPosition().getAltitude();
            e.mettreAJour(this);
            if (longitude != e.getPosition().getLongitude()
                    || altitude != e.getPosition().getAltitude()) {
                eventHandler.send(new EntiteDeplaceeEvent(this, e));
            }
        }
        tempsCourant++;
        eventHandler.send(new PasDeTempsEvent(this, tempsCourant));
    }

    /** @throws IllegalArgumentException si l'identifiant est deja utilise */
    public void ajouter(Entite entite) {
        if (entites.containsKey(entite.getId())) {
            throw new IllegalArgumentException("Identifiant deja utilise : " + entite.getId());
        }
        entites.put(entite.getId(), entite);
        eventHandler.send(new EntiteAjouteeEvent(this, entite));
    }

    /** @throws IllegalArgumentException si l'identifiant est inconnu */
    public void supprimer(String id) {
        Entite entite = entites.remove(id);
        if (entite == null) {
            throw new IllegalArgumentException("Entite inconnue : " + id);
        }
        eventHandler.send(new EntiteSupprimeeEvent(this, entite));
    }

    /**
     * Le gestionnaire auquel s'enregistrer. Les evenements passes ne sont
     * pas rejoues : pour connaitre l'etat initial, lire {@link #getEntites()}
     * avant de s'enregistrer.
     */
    public EventHandler getEventHandler() {
        return eventHandler;
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
