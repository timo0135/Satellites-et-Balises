package vue;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import javax.swing.BorderFactory;

import model.Entite;
import model.Simulation;
import nicellipse.component.NiRectangle;

/**
 * Vue de la simulation : un rectangle Nicellipse (ciel) contenant la mer et
 * une {@link VueEntite} par entite.
 * <p>
 * Patron Observer : elle est prevenue apres chaque pas de temps et
 * synchronise ses vues avec le modele (creation, deplacement, suppression).
 * <p>
 * Le choix de la vue d'une entite passe par un registre type -&gt; fabrique :
 * ajouter un nouveau type d'entite revient a enregistrer sa vue, sans
 * modifier cette classe.
 */
public class VueSimulation extends NiRectangle implements Observateur {
    private static final long serialVersionUID = 1L;

    private final transient Repere repere;
    private final transient Map<Entite, VueEntite> vues = new LinkedHashMap<>();
    private final transient Map<Class<? extends Entite>, Function<Entite, VueEntite>> fabriques =
            new HashMap<>();

    public VueSimulation(Repere repere) {
        this.repere = repere;
        this.setBounds(0, 0, repere.getLargeurPx(), repere.getHauteurPx());
        this.setBackground(new Color(0xE6F1FB));

        int niveauMer = repere.ordonnee(0);
        NiRectangle mer = new NiRectangle();
        mer.setBorder(BorderFactory.createEmptyBorder());
        mer.setBackground(new Color(0x85B7EB));
        mer.setBounds(0, niveauMer, repere.getLargeurPx(), repere.getHauteurPx() - niveauMer);
        this.add(mer);
    }

    /** Declare quelle vue utiliser pour un type d'entite. */
    public <T extends Entite> void enregistrer(Class<T> type,
                                               Function<? super T, ? extends VueEntite> fabrique) {
        fabriques.put(type, entite -> fabrique.apply(type.cast(entite)));
    }

    @Override
    public void miseAJour(Simulation simulation) {
        Iterator<Map.Entry<Entite, VueEntite>> it = vues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Entite, VueEntite> e = it.next();
            if (!simulation.getEntites().contains(e.getKey())) {
                this.remove(e.getValue());
                it.remove();
            }
        }
        for (Entite entite : simulation.getEntites()) {
            VueEntite vue = vues.get(entite);
            if (vue == null) {
                vue = creerVue(entite);
                if (vue == null) {
                    continue;
                }
                vues.put(entite, vue);
                this.add(vue, 0); // index 0 : peint au-dessus de la mer
            }
            vue.synchroniser(repere);
        }
        this.repaint();
    }

    private VueEntite creerVue(Entite entite) {
        Function<Entite, VueEntite> fabrique = fabriques.get(entite.getClass());
        return fabrique == null ? null : fabrique.apply(entite);
    }
}
