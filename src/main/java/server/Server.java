package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * S'occupe des opérations côté serveur
 */
public class Server extends Thread {

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
     * @param h nouvel eventHandler
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) throws IOException, ClassNotFoundException {
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
                new Thread(new ClientLauncher()).start();
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
    /*public static void main(String args[]) throws IOException {
        Server thread1=new Server(1337);
        thread1.start();
    }*/

    /**
     * Récupère la commande envoyée par le client.
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
     * @param cmd commande envoyée par le client
     * @param arg argument entré par l'utilisateur
     * @throws IOException Si handleRegistration rencontre une erreur d'input ou d'output
     * @throws ClassNotFoundException Si handleRegistration utilise une classe introuvable
     */
    public void handleEvents(String cmd, String arg) throws IOException, ClassNotFoundException {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de
     l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg)  {

        //Trouver le fichier contenant la liste des cours disponibles
        String filePath = "src/main/java/server/data/cours.txt";

        try {

            //Lire le fichier et récupérer les cours
            FileReader listeDesCours = new FileReader(filePath);
            BufferedReader lectureDuFichier = new BufferedReader(listeDesCours);
            String unCours;

            ArrayList<String> lesCours = new ArrayList<String>();

            //Trier et prendre les cours nécessaires en fonction de la session demandée
            switch (arg) {

                case "1":
                    while ((unCours = lectureDuFichier.readLine()) != null) {
                        if (unCours.contains("Automne")) {
                            lesCours.add(unCours);
                        }
                    }
                    break;

                case "2":
                    while ((unCours = lectureDuFichier.readLine()) != null) {
                        if (unCours.contains("Hiver")) {
                            lesCours.add(unCours);
                        }
                    }
                    break;

                case "3":
                    while ((unCours = lectureDuFichier.readLine()) != null) {
                        if (unCours.contains("Ete")) {
                            lesCours.add(unCours);
                        }
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Session invalide.");

            }
            listeDesCours.close();
            lectureDuFichier.close();

            //Placement des cours dans des objets Cours
            ArrayList<Course> Cours = new ArrayList<Course>();
            for (int i=0; i<lesCours.size(); i++) {
                String aCourse = (String) lesCours.get(i);
                String[] words = aCourse.split("\\s+");
                ArrayList<String> separations = new ArrayList<String>();
                for (String word : words) {
                    separations.add(word);
                }
                Cours.add(new Course(separations.get(1), separations.get(0), separations.get(2)));
            }

            //Envoi de l'objet
            System.out.println(Cours+"BEFORE PERHAPS");
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
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer
     dans un fichier texte et renvoyer un message de confirmation au client. La méthode gére les exceptions si
     une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     @throws IOException Si il y a une erreur d'input ou d'output lors de la lecture de RegistrationForm
     @throws ClassNotFoundException Si l'objet RegistrationForm est introuvable
     */
    public void handleRegistration() throws IOException, ClassNotFoundException {

        RegistrationForm registrationForm = (RegistrationForm) objectInputStream.readObject();

        String filename = "src/main/java/server/data/inscription.txt";
        BufferedReader reader = null;


        FileWriter fw = null;
        BufferedWriter writer = null;


        fw = new FileWriter("src/main/java/server/data/inscription.txt", true);
        writer = new BufferedWriter(fw);
        String s = (registrationForm.getCourse().getSession() + "\t" + registrationForm.getCourse().getCode() + "\t" + registrationForm.getMatricule() + "\t" + registrationForm.getPrenom() + "\t" + registrationForm.getNom() + "\t" + registrationForm.getEmail());

        writer.append("\n\n" + s);

        String msg = ("Félicitations! Inscription réussie de " + registrationForm.getPrenom() + " au cours " + registrationForm.getCourse().getCode());
        System.out.println(msg);


        // Close both the writer and the file writer, even if an exception was thrown
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                // Handle the exception
            }
        }
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException e) {
                // Handle the exception
            }
        }
    }
}
