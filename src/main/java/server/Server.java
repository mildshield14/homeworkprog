package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) throws IOException, ClassNotFoundException {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
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

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

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
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        // TODO: implémenter cette méthode

        String filePath = "src/main/java/server/data/cours.txt";
        try {
            FileReader listeDesCours = new FileReader(filePath);
            BufferedReader lectureDuFichier = new BufferedReader(listeDesCours);
            String unCours;

            ArrayList<String> lesCours = new ArrayList<String>();

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
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() throws IOException, ClassNotFoundException {
        // TODO: implémenter cette méthode

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
