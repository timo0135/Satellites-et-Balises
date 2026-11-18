package model.activite;

import model.balise.Balise;

/**
 * Patron State : activite courante d'une balise. Chaque etat agit puis
 * decide lui-meme de l'etat suivant.
 * <p>
 * Cycle : {@link Collecte} -&gt; {@link Remontee} -&gt; {@link Transmission}
 * -&gt; {@link Collecte}.
 */
public interface Activite {
    /** Vitesse verticale (par pas de temps) des remontees et descentes. */
    double VITESSE_VERTICALE_PAR_DEFAUT = 5.0;

    /** Effectue le travail de l'activite pour un pas de temps. */
    void agir(Balise balise);

    /** Renvoie l'activite a utiliser au pas suivant (this si inchangee). */
    Activite suivante(Balise balise);
}
