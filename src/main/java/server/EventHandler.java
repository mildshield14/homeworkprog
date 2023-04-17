package server;

@FunctionalInterface
/**
 * Réagit aux requêtes
 * @param cmd commande associée à la saisie de l'utilisateur
 * @param arg saisie de l'utilisateur
 */
public interface EventHandler {
    void handle(String cmd, String arg);
}