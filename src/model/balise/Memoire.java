package model.balise;

import java.util.ArrayList;
import java.util.List;

import model.mesure.Mesure;

public class Memoire {
    private final int capacite;
    private final List<Mesure> mesures = new ArrayList<>();

    public Memoire(int capacite) {
        if (capacite <= 0) {
            throw new IllegalArgumentException("La capacite doit etre positive");
        }
        this.capacite = capacite;
    }

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
        return mesures;
    }
}
