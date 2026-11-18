package vue;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import javax.swing.BorderFactory;

import eventHandler.EventHandler;
import model.Entite;
import model.Simulation;
import model.evenement.EntiteAjouteeEvent;
import model.evenement.EntiteDeplaceeEvent;
import model.evenement.EntiteSupprimeeEvent;
import model.evenement.PasDeTempsEvent;
import model.evenement.SimulationListener;
import nicellipse.component.NiRectangle;

/**
 * Vue de la simulation : un rectangle Nicellipse (ciel) contenant la mer et
 * une {@link VueEntite} par entite.
 * <p>
 * C'est un {@link SimulationListener} : elle reagit aux evenements de la
 * simulation. Une entite ajoutee cree sa vue, une entite deplacee replace la
 * sienne, une entite supprimee retire la sienne, et chaque pas de temps
 * declenche un seul rafraichissement.
 * <p>
 * Le choix de la vue d'une entite passe par un registre type -&gt; fabrique :
 * ajouter un nouveau type d'entite revient a enregistrer sa vue, sans
 * modifier cette classe.
 */
public class VueSimulation extends NiRectangle implements SimulationListener {
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

    /**
     * Commence a suivre une simulation : affiche les entites deja presentes
     * (les evenements passes ne sont pas rejoues), puis s'enregistre aupres
     * de son gestionnaire d'evenements.
     */
    public void suivre(Simulation simulation) {
        for (Entite entite : simulation.getEntites()) {
            ajouterVue(entite);
        }
        EventHandler eventHandler = simulation.getEventHandler();
        eventHandler.registerListener(EntiteAjouteeEvent.class, this);
        eventHandler.registerListener(EntiteSupprimeeEvent.class, this);
        eventHandler.registerListener(EntiteDeplaceeEvent.class, this);
        eventHandler.registerListener(PasDeTempsEvent.class, this);
    }

    @Override
    public void entiteAjoutee(EntiteAjouteeEvent e) {
        ajouterVue(e.getEntite());
    }

    @Override
    public void entiteSupprimee(EntiteSupprimeeEvent e) {
        VueEntite vue = vues.remove(e.getEntite());
        if (vue != null) {
            this.remove(vue);
            this.repaint();
        }
    }

    @Override
    public void entiteDeplacee(EntiteDeplaceeEvent e) {
        VueEntite vue = vues.get(e.getEntite());
        if (vue != null) {
            vue.synchroniser(repere);
        }
    }

    @Override
    public void pasDeTempsEcoule(PasDeTempsEvent e) {
        this.repaint();
    }

    private void ajouterVue(Entite entite) {
        Function<Entite, VueEntite> fabrique = fabriques.get(entite.getClass());
        if (fabrique == null) {
            return; // type d'entite sans representation : on l'ignore
        }
        VueEntite vue = fabrique.apply(entite);
        vue.synchroniser(repere);
        vues.put(entite, vue);
        this.add(vue, 0); // index 0 : peint au-dessus de la mer
        this.repaint();
    }
}
