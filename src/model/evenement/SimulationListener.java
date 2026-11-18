package model.evenement;

/**
 * Ce qu'un ecouteur peut apprendre de la simulation. Toutes les methodes
 * sont vides par defaut : un ecouteur ne redefinit que celles qui
 * l'interessent, et s'enregistre pour les evenements correspondants.
 */
public interface SimulationListener {

	default void entiteAjoutee(EntiteAjouteeEvent e) {}

	default void entiteSupprimee(EntiteSupprimeeEvent e) {}

	default void entiteDeplacee(EntiteDeplaceeEvent e) {}

	default void pasDeTempsEcoule(PasDeTempsEvent e) {}
}
