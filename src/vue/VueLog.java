package vue;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

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
 * Seconde vue de la simulation, sous forme de journal : une ligne de texte par
 * evenement recu. Comme {@link VueSimulation}, c'est un
 * {@link SimulationListener}, mais elle ecrit au lieu de dessiner : le modele
 * ne sait pas qu'il a deux vues.
 * <p>
 * Format des lignes :
 * <pre>
 * + S1 (Satellite) en (0, 220)     entite ajoutee (ou presente au demarrage)
 * - B4 (Balise)                    entite supprimee
 * &gt; S1 en (3, 220)                entite deplacee
 * -- t=12 --                       fin du pas de temps 12
 * </pre>
 * Des cases a cocher filtrent les types d'evenements, et le journal garde
 * seulement les {@value #MAX_LIGNES} dernieres lignes.
 */
public class VueLog extends NiRectangle implements SimulationListener {
    private static final long serialVersionUID = 1L;

    private static final int MAX_LIGNES = 500;
    private static final int HAUTEUR_COMMANDES = 60;

    private final JTextArea texte = new JTextArea();
    private final JCheckBox entites = new JCheckBox("Entités", true);
    private final JCheckBox deplacements = new JCheckBox("Déplacements", true);
    private final JCheckBox pas = new JCheckBox("Pas de temps", true);

    public VueLog(int largeur, int hauteur) {
        this.setBounds(0, 0, largeur, hauteur);

        JButton vider = new JButton("Vider");
        vider.addActionListener(e -> texte.setText(""));

        JPanel commandes = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        commandes.add(entites);
        commandes.add(deplacements);
        commandes.add(pas);
        commandes.add(vider);
        commandes.setBounds(0, 0, largeur, HAUTEUR_COMMANDES);

        texte.setEditable(false);
        texte.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        JScrollPane defilement = new JScrollPane(texte);
        defilement.setBounds(0, HAUTEUR_COMMANDES, largeur, hauteur - HAUTEUR_COMMANDES);
        defilement.setPreferredSize(new Dimension(largeur, hauteur - HAUTEUR_COMMANDES));

        this.add(commandes);
        this.add(defilement);
    }

    /**
     * Commence a suivre une simulation : note les entites deja presentes (les
     * evenements passes ne sont pas rejoues), puis s'enregistre aupres de son
     * gestionnaire d'evenements.
     */
    public void suivre(Simulation simulation) {
        ecrire("-- t=" + simulation.getTempsCourant() + " (état initial) --");
        for (Entite entite : simulation.getEntites()) {
            ecrire("+ " + decrire(entite) + " en " + position(entite));
        }
        EventHandler eventHandler = simulation.getEventHandler();
        eventHandler.registerListener(EntiteAjouteeEvent.class, this);
        eventHandler.registerListener(EntiteSupprimeeEvent.class, this);
        eventHandler.registerListener(EntiteDeplaceeEvent.class, this);
        eventHandler.registerListener(PasDeTempsEvent.class, this);
    }

    @Override
    public void entiteAjoutee(EntiteAjouteeEvent e) {
        if (entites.isSelected()) {
            ecrire("+ " + decrire(e.getEntite()) + " en " + position(e.getEntite()));
        }
    }

    @Override
    public void entiteSupprimee(EntiteSupprimeeEvent e) {
        if (entites.isSelected()) {
            ecrire("- " + decrire(e.getEntite()));
        }
    }

    @Override
    public void entiteDeplacee(EntiteDeplaceeEvent e) {
        if (deplacements.isSelected()) {
            ecrire("> " + e.getEntite().getId() + " en " + position(e.getEntite()));
        }
    }

    @Override
    public void pasDeTempsEcoule(PasDeTempsEvent e) {
        if (pas.isSelected()) {
            ecrire("-- t=" + e.getTempsCourant() + " --");
        }
    }

    /** Le journal complet, tel qu'affiche (utile pour les tests). */
    public String getContenu() {
        return texte.getText();
    }

    private static String decrire(Entite entite) {
        return entite.getId() + " (" + entite.getClass().getSimpleName() + ")";
    }

    private static String position(Entite entite) {
        return String.format("(%.0f, %.0f)", entite.getPosition().getLongitude(),
                entite.getPosition().getAltitude());
    }

    private void ecrire(String ligne) {
        texte.append(ligne + "\n");
        limiter();
        texte.setCaretPosition(texte.getDocument().getLength()); // defile jusqu'a la fin
    }

    /** Supprime les lignes les plus anciennes au-dela de {@value #MAX_LIGNES}. */
    private void limiter() {
        int lignes = texte.getLineCount() - 1; // la derniere « ligne » est vide
        int exces = lignes - MAX_LIGNES;
        if (exces > 0) {
            try {
                texte.replaceRange("", 0, texte.getLineEndOffset(exces - 1));
            } catch (BadLocationException e) {
                throw new IllegalStateException(e); // impossible : l'indice vient du document
            }
        }
    }
}
