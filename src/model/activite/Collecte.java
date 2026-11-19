package model.activite;

import model.balise.Balise;
import model.balise.Memoire;
import model.instrument.Instrument;

public class Collecte implements Activite {

    public static final int PERIODE_PAR_DEFAUT = 10;

    private final int periode;
    private int pas;

    public Collecte() {
        this(PERIODE_PAR_DEFAUT);
    }

    public Collecte(int periode) {
        if (periode <= 0) {
            throw new IllegalArgumentException("La periode doit etre positive");
        }
        this.periode = periode;
    }

    @Override
    public void agir(Balise balise) {
        balise.getDeplacementActuel().deplacer(balise);

        boolean releve = pas % periode == 0; // un releve des le premier pas
        pas++;
        if (!releve) {
            return;
        }
        Memoire memoire = balise.getMemoire();
        for (Instrument instrument : balise.getInstruments()) {
            if (memoire.estPleine()) {
                break;
            }
            memoire.ajouter(instrument.mesurer(balise.creerContexte()));
        }
    }

    @Override
    public Activite suivante(Balise balise) {
        if (balise.getMemoire().estPleine()) {
            return new Remontee();
        }
        return this;
    }

    public int getPeriode() {
        return periode;
    }

    public int getPas() {
        return pas;
    }
}
