package server;

import java.io.IOException;
import java.net.Socket;

/**
 * Lance le client
 */
public class ClientLauncher extends Thread {
    protected static Socket socket;

    /**
     * Le port utilisé
     */
    public static final Integer port = 1337;

    /**
     *
     */
    public static final String address = "localhost";

    /**
     * Permet de déconnecter le client
     * @throws IOException Si le port ou l'adresse sont incorrects
     */
    public static void closeSocket() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    /**
     * Récupère le socket
     * @return Retourne le socket
     */
    public static Socket getSocket() {
        return socket;
    }

    /**
     * Lance le client
     * @param args
     */

    public static void main(String[] args) {

        //Client client;

            try {
                Client thread1=new Client(port);
                //client = new Client(port);
                System.out.println("Client is running...");
                thread1.prepTransfer();
                thread1.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

