package server;

/**
 * Lance le serveur
 */
public class ServerLauncher {

    /**
     * Le port utilisé par le serveur
     */
    public final static int PORT = 1237;

    /**
     * Démarre le serveur
     * @param args port désiré
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
