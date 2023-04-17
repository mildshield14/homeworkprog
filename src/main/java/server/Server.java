package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * S'occupe des opérations côté serveur
 */
public class Server {

    /**
     * Identifie le string "INSCRIRE" en tant que "REGISTER_COMMAND"
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";

    /**
     * Identifie le string "CHARGER" en tant que "LOAD_COMMAND"
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Crée le socket du serveur et groupe les enventHandlers du serveur
     *
     * @param port le port à utiliser
     * @throws IOException Si une erreur d'input ou d'output arrive au moment de la création du socket.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Permet de gérer les évènements
     *
     * @param h nouvel eventHandler
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Envoie les commandes recues aux event handlers
     *
     * @param cmd commande recue
     * @param arg argument recu
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Permet d'établir et de gérer la connexion avec le client.
     */
    public void run() {

        while (true) {
            try {
                client = server.accept();
                server.setReuseAddress(true);
                System.out.println("Connecté au client: " + client);

                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Récupère la commande envoyée par le client.
     *
     * @throws IOException
     * @throws ClassNotFoundException Si l'objet est introuvable
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Appaire la commande et l'argument ensemble pour traiter les requêtes du client.
     *
     * @param line Ligne de commande
     * @return retourne la paire contenant la commande et l'argument
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Déconnecte le client.
     *
     * @throws IOException Si la méthode rencontre une erreur d'input ou d'output.
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }


    /**
     * Agit en conséquence lorsqu'une requête de chargement (CHARGER) ou d'inscription (INSCRIRE)
     * est envoyée au serveur.
     *
     * @param cmd commande envoyée par le client
     * @param arg argument entré par l'utilisateur
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     * La méthode filtre les cours par la session spécifiée en argument.
     * Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     * La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de
     * l'objet dans le flux.
     *
     * @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {

        try {

            //Lire le fichier et récupérer les cours
            String filePath = "src/main/java/server/data/cours.txt";

            Scanner scanner = new Scanner(new File(filePath), StandardCharsets.UTF_8);

            ArrayList<String> lesCours = new ArrayList<String>();
            //Trier et prendre les cours nécessaires en fonction de la session demandée
            switch (arg) {
                case "1":
                    while (scanner.hasNextLine()) {
                        String unCours = scanner.nextLine();
                        if (unCours.contains("Automne")) {
                            lesCours.add(unCours);
                        }
                    }

                    scanner.close();
                    break;

                case "2":
                    while (scanner.hasNextLine()) {
                        String unCours = scanner.nextLine();
                        if (unCours.contains("Hiver")) {
                            lesCours.add(unCours);
                        }
                    }

                    scanner.close();
                    break;

                case "3":
                    while (scanner.hasNextLine()) {
                        String unCours = scanner.nextLine();
                        if (unCours.contains("Ete")) {
                            lesCours.add(unCours);
                        }
                    }

                    scanner.close();
                    break;

                default:
                    throw new IllegalArgumentException("Session invalide.");

            }

            //Placement des cours dans des objets Cours
            ArrayList<Course> Cours = new ArrayList<Course>();
            for (int i = 0; i < lesCours.size(); i++) {
                String aCourse = (String) lesCours.get(i);
                String[] words = aCourse.split("\\s+");
                ArrayList<String> separations = new ArrayList<String>();
                for (String word : words) {
                    separations.add(word);
                }
                Cours.add(new Course(separations.get(1), separations.get(0), separations.get(2)));
            }

            //Envoi de l'objet
            objectOutputStream.writeObject(Cours);
            System.out.println(Cours);
            objectOutputStream.flush();


        } catch (FileNotFoundException ex) {
            System.out.println("Le fichier est introuvable.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer
     * dans un fichier texte et renvoyer un message de confirmation au client. La méthode gére les exceptions si
     * une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        

        try {
            RegistrationForm registrationForm = (RegistrationForm) objectInputStream.readObject();
            String filename = "src/main/java/server/data/inscription.txt";
            BufferedReader reader = null;


            FileOutputStream fos = null;
            BufferedWriter writer = null;

            try {
                fos = new FileOutputStream(filename, true);
                writer = new BufferedWriter(new OutputStreamWriter(fos));
                String s = (registrationForm.getCourse().getSession() + "\t" + registrationForm.getCourse().getCode() + "\t" + registrationForm.getMatricule() + "\t" + registrationForm.getPrenom() + "\t" + registrationForm.getNom() + "\t" + registrationForm.getEmail());

                writer.append("\n\n" + s);
                writer.flush();
                String msg = ("Félicitations! Inscription réussie de " + registrationForm.getPrenom() + " au cours " + registrationForm.getCourse().getCode());
                System.out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close both the writer and the file output stream, even if an exception was thrown
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        // Handle the exception
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        // Handle the exception
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
