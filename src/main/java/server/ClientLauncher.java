package server;

import java.io.IOException;
import java.net.Socket;

/**
 * Lance le client
 */
public class ClientLauncher {
    protected static Socket socket;

    /**
     * Le port utilisé
     */
    public static final Integer port = 1237;

    /**
     *
     */
    public static final String address = "localhost";

    /**
     * Permet de déconnecter le client
     *
     * @throws IOException Si le port ou l'adresse sont incorrects
     */
    public static void closeSocket() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    /**
     * Récupère le socket
     *
     * @return Retourne le socket
     */
    public static Socket getSocket() {
        return socket;
    }

    /**
     * Lance le client
     *
     * @param args
     */

    public static void main(String[] args) {


        Client client;


        try {

            client = new Client(port);
            System.out.println("Client is running...");
            client.prepTransfer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setConnection() throws IOException {
        socket = new Socket(address, (port));

    }

}

