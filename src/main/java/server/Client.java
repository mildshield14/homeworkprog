package server;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * S'occupe des opérations côté client
 */
public class Client  {

    /**
     * Met la liste de cours recue dans un ArrayList
     */
    public static ArrayList<Course> courseEntry;

    /**
     * Message de confirmation ou non après inscription
     */
    public static String message;

    /**
     * Socket du client
     */
    public static Socket client;

    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    /**
    * Ouvre et ferme les streams et le socket
     * @throws IOException Si il y a une erreur dans les streams
     */
    public static void justNeedSocket() throws IOException {

        if (client != null && !client.isClosed()) {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (client != null && !client.isClosed()) {
                client.close();
            }
        }
        if (client == null || client.isClosed()) {
            client = new Socket("localhost", 1237);

        }
        objectOutputStream = new ObjectOutputStream(client.getOutputStream());

    }

    /**
     * Connecte le client
     * @throws IOException
     */
    public void prepTransfer() throws IOException {

        if (client == null || client.isClosed()) {
            client = new Socket("localhost", 1237);

        }
        objectOutputStream = new ObjectOutputStream(client.getOutputStream());

        objectOutputStream.flush();

        run();

        finishTransfer();

    }

   

    /**
     * Met fin aux echanges du client via le socket
     * @throws IOException
     */
    public static void finishTransfer () throws IOException {
        if (client != null && !client.isClosed()) {
            client.close();
        }
        if (objectInputStream != null) {
            objectInputStream.close();
        }
        if (objectOutputStream != null) {
            objectOutputStream.close();
        }
        if (ClientLauncher.getSocket() != null) {
            ClientLauncher.closeSocket();
        }

    }

    
    /**
         * Permet de lancer le GUI
         * @param entry
         * @return
         */
        public static String startGUI (ArrayList entry){
            try {

                Scanner scanner = new Scanner(System.in); //to pass to procedure// will be null

                String com = "";
                Boolean done = true;

                com = "INSCRIRE";
                justNeedSocket();
                inscrire(true, com, done, entry);
                scanner.close();

                return GUIMessage();

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        /**
         * Retourne le message de confirmation sur le GUI
         * @return
         */
        public static String GUIMessage () {
            return message;
        }


        /**
         * Retourne la liste des cours
         * @return
         */
        public static ArrayList arraylist () {
            return courseEntry;
        }

        /**
         * Crée une ArrayList qui contiendra les données du GUI
         * @param courseEntry1
         */
        public static void setCourseEntry (ArrayList < Course > courseEntry1) {
            courseEntry = courseEntry1;
        }


        /** Recupere le socket pour connecter le client
         *
         * @param port
         * @throws IOException
         */
    public Client( int port) throws IOException {
        ClientLauncher.setConnection();

        this.client = ClientLauncher.getSocket();


        }

        /**
         * Commence le programme principal et prend les entrees de l'utilisateur
         */
        public void run () {

            boolean go = true;

            while (go == true) {
                try {


                    Scanner scanner = new Scanner(System.in);

                    String com = "";
                    Boolean done = true;

                    System.out.println("*** Bienvenue au portail de cours de l'UDEM *** \n"
                            + "Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours: \n"
                            + "1: Automne \n" + "2: Hiver \n" + "3: Ete");

                    System.out.print("> Choix: ");

                    com = scanner.nextLine();

                    Pattern choix1 = Pattern.compile("[1-3]");
                    Matcher choixSession = choix1.matcher(com);
                    boolean validSession = choixSession.find();
                    if (!validSession) {
                        throw new IllegalArgumentException("Erreur. Session invalide.");
                    }

                    if (com.equals("1") || com.equals("2") || com.equals("3")) {

                        connect("CHARGER " + com);
                        System.out.println("OK!"+com);

                        try {
                            objectInputStream = new ObjectInputStream(client.getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ArrayList<Course> leCours = (ArrayList<Course>) objectInputStream.readObject();


                        setCourseEntry(leCours);


                        System.out.println("1: Consulter les cours offerts pour une autre session \n" +
                                "2: Inscription à un cours \n");
                        com = scanner.nextLine();
                        System.out.print("> Choix :" + com);

                        Pattern choix2 = Pattern.compile("[1-2]");
                        Matcher choixInscription = choix2.matcher(com);
                        boolean validInscription = choixInscription.find();
                        if (!validInscription) {
                            throw new IllegalArgumentException("Erreur. Choix invalide.");
                        }

                        if (com.equals("1")) {
                            com = "1";
                            go = true;
                            finishTransfer();
                            prepTransfer();
                        } else if (com.equals("2")) {
                            com = "2";
                            go = false;
                        }
                        System.out.println(leCours);

                    }


                    if (com.equals("2")) {
                        com = "INSCRIRE";
                        ArrayList<String> entry = new ArrayList<>(); //Utilisation pour le GUI; mais ici il sera null

                        inscrire(false, com, done, entry);

                    }


                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        }


        /**
         * Charge les cours
         * @param com la session desiree
         * @return Retourne les cours de la session choisie
         * @throws IOException Si il y a un probleme avec la liste de cours
         * @throws ClassNotFoundException Si la liste de cours est introuvable
         */
        public static ArrayList<Course> charger1 (String com) throws IOException, ClassNotFoundException {

            // create a socket on the specified port+ object out and in
            connect("CHARGER " + com);

            objectInputStream = new ObjectInputStream(client.getInputStream());

            ArrayList<Course> Cours = (ArrayList<Course>) objectInputStream.readObject();

            setCourseEntry(Cours);

            System.out.println(Cours);
            finishTransfer();

            return arraylist();


        }

    /**
    * Active la connection du client
    */
        public static void connect (String mot){
            try {
                justNeedSocket();
                objectOutputStream.writeObject(mot);
                objectOutputStream.flush();
            } catch (IOException e) {
                // Handle the exception
                System.out.println(mot);
                e.printStackTrace();
            }
        }


        /**
         * Enregistre les reponses du client et effectue l'inscription
         * @param dev3
         * @param com session choisie
         * @param done
         * @param entry la liste de cours
         * @throws IOException Si la liste de cours est mal entree
         */
        public static void inscrire (Boolean dev3, String com, Boolean done, ArrayList entry) throws IOException {


            while (done) {

                if (dev3 != true) {

                    //On lit la réponse à chaque champ et on verifie si l'utilisateur a respecté le format demandé
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Veuillez saisir votre prénom: ");
                    String firstName = scanner.nextLine();
                    Pattern nums = Pattern.compile("\\d|^$");
                    Matcher prenom = nums.matcher(firstName);
                    boolean numsInPrenom = prenom.find();
                    if (numsInPrenom) {
                        throw new IllegalArgumentException("Erreur. Prénom invalide.");
                    }

                    System.out.println("Veuillez saisir votre nom: ");
                    String lastName = scanner.nextLine();
                    Matcher nom = nums.matcher(lastName);
                    boolean numsInNom = nom.find();
                    if (numsInNom) {
                        throw new IllegalArgumentException("Erreur. Nom invalide.");
                    }

                    System.out.println("Veuillez saisir votre email: ");
                    String email = scanner.nextLine();
                    Pattern mail = Pattern.compile(".+[^.]@.+[.][a-z][a-z][a-z]?$");
                    Matcher courriel = mail.matcher(email);
                    boolean correctEmail = courriel.find();
                    if (!correctEmail) {
                        throw new IllegalArgumentException("Erreur. E-mail invalide.");
                    }

                    System.out.println("Veuillez saisir votre matricule: ");
                    String matricule = scanner.nextLine();
                    Pattern mat = Pattern.compile("[0-9]{6}");
                    Matcher matriculeEtu = mat.matcher(matricule);
                    boolean correctMatricule = matriculeEtu.find();
                    if (!correctMatricule) {
                        throw new IllegalArgumentException("Erreur. Matricule invalide.");
                    }

                    System.out.println("Veuillez saisir le code du cours: ");
                    String courseName = scanner.nextLine();
                    Pattern code = Pattern.compile("^[a-zA-Z]{3}[0-9]{4}$");
                    Matcher codeCours = code.matcher(courseName);
                    boolean correctCode = codeCours.find();
                    if (!correctCode) {
                        throw new IllegalArgumentException("Erreur. Le code de cours entré est invalide.");
                    }


                    ArrayList<Course> courseEn = arraylist();


                    int size = courseEn.size();
                    Course courInfo = new Course("", "", "");
                    for (int i = 0; i < size; i++) {

                        if (courseEn.get(i).getCode().equals(courseName)) {
                            courInfo = courseEn.get(i);
                        }

                    }
                    if (courInfo.getName() != "") {
                        RegistrationForm userRegistration = new RegistrationForm(firstName, lastName, email, matricule, courInfo);

                        connect("INSCRIRE");

                        objectOutputStream.writeObject(userRegistration);

                        objectOutputStream.flush();
                        finishTransfer();
                        String msg = ("Félicitations! Inscription réussie de " + userRegistration.getPrenom() + " au cours " + userRegistration.getCourse().getCode());
                        System.out.println(msg);
                        finishTransfer();

                    } else {
                        System.out.println("Ce cours n'est malheureusement pas disponible.");
                        connect("INSCRIRE");
                        objectOutputStream.writeObject("ERROR");
                        objectOutputStream.flush();
                        finishTransfer();
                    }
                } else {
                    justNeedSocket();
                    String prenomGUI = entry.get(0).toString();
                    String nomGUI = entry.get(1).toString();
                    String emailGUI = entry.get(2).toString();
                    String matriculeGUI = entry.get(3).toString();
                    String courGUI = entry.get(4).toString();

                    System.out.println(courGUI);


                    ArrayList<Course> courseEn = arraylist();

                    int size = courseEn.size();
                    Course courInfo = new Course("", "", "");
                    for (int i = 0; i < size; i++) {

                        if (courseEn.get(i).getCode().equals(courGUI)) {
                            courInfo = courseEn.get(i);
                        }

                    }
                    if (courInfo.getName() != "") {
                        RegistrationForm userRegistration = new RegistrationForm(prenomGUI, nomGUI, emailGUI, matriculeGUI, courInfo);
                        connect("INSCRIRE");
                        objectOutputStream.writeObject(userRegistration);
                        objectOutputStream.flush();

                        String msg = ("Félicitations! Inscription réussie de " + userRegistration.getPrenom() + " au cours " + userRegistration.getCourse().getCode());

                        message = (msg);
                        System.out.println(msg);
                        finishTransfer();
                    } else {
                        message = ("");
                        System.out.println("Ce cours n'est malheureusement pas disponible.");
                        connect("INSCRIRE");
                        objectOutputStream.writeObject("ERROR");
                        objectOutputStream.flush();
                        finishTransfer();
                    }

                }


                done = false;


            }
            finishTransfer();

        }
    }
