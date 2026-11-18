package model.balise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.mesure.Mesure;

/** Memoire de capacite limitee (en nombre de mesures) d'une balise. */
public class Memoire {
    private final int capacite;
    private final List<Mesure> mesures = new ArrayList<>();

    public Memoire(int capacite) {
        if (capacite <= 0) {
            throw new IllegalArgumentException("La capacite doit etre positive");
        }
        this.capacite = capacite;
    }

    /** @throws IllegalStateException si la memoire est pleine */
    public void ajouter(Mesure mesure) {
        if (estPleine()) {
            throw new IllegalStateException("Memoire pleine");
        }
        mesures.add(mesure);
    }

    public boolean estPleine() {
        return mesures.size() >= capacite;
    }

    public boolean estVide() {
        return mesures.isEmpty();
    }

    /** Retire et renvoie toutes les mesures conservees. */
    public List<Mesure> vider() {
        List<Mesure> contenu = new ArrayList<>(mesures);
        mesures.clear();
        return contenu;
    }

    public int getCapacite() {
        return capacite;
    }

    public int taille() {
        return mesures.size();
    }

    public List<Mesure> getMesures() {
        return Collections.unmodifiableList(mesures);
    }
}
