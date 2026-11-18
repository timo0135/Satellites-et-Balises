package vue;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

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
 * Vue de la simulation : un rectangle Nicellipse dont le fond est un decor
 * ({@link FondMonde}) et qui contient une {@link VueEntite} par entite.
 * <p>
 * Ordre de dessin : le decor, puis le decor propre a chaque entite (traines,
 * traits de profondeur), puis les entites elles-memes par-dessus.
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
    private final transient FondMonde fond;
    private final transient Map<Entite, VueEntite> vues = new LinkedHashMap<>();
    private final transient Map<Class<? extends Entite>, Function<Entite, VueEntite>> fabriques =
            new HashMap<>();

    public VueSimulation(Repere repere) {
        this.repere = repere;
        this.fond = new FondMonde(repere);
        this.setBounds(0, 0, repere.getLargeurPx(), repere.getHauteurPx());
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
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        fond.dessiner(g);
        for (VueEntite vue : vues.values()) {
            vue.dessinerDecor(g, repere);
        }
        g.dispose();
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
        this.add(vue, 0); // index 0 : peint au-dessus du decor
        this.repaint();
    }
}
