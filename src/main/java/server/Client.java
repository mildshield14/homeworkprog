package server;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    public static ArrayList<Course> courseEntry;
    public static String message;
    public static Boolean ins;
    public static Socket client;

    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    public static ArrayList arraylist(){return courseEntry;}

    public static String GUIMessage(){
        return message;
    }

    public static void setCourseEntry(ArrayList<Course> courseEntry1) {
        courseEntry = courseEntry1;
    }

    public Client(int port) throws IOException {

        ClientLauncher.setConnection();

        this.client = ClientLauncher.getSocket();

    }

    // on utilisera cela surtout pour le GUI car on ne veut pas passer par les entrees de run();
    public static void justNeedSocket() throws IOException {

        if (client != null && !client.isClosed()) {
            objectInputStream.close();
            objectOutputStream.close();
            client.close();
        }
        if (client == null || client.isClosed()) {
            client = new Socket("localhost", 1337);

        }
        objectOutputStream = new ObjectOutputStream(client.getOutputStream());

        objectInputStream = new ObjectInputStream(client.getInputStream());


    }

    // Ceci permet de commencer les entrees en preparant la connection puis lancant run();
    //run() etant le module qui fait explicitement les entrees.
    public static void prepTransfer() throws IOException {

        if (client == null || client.isClosed()) {
            client = new Socket("localhost", 1337);
        }

        objectOutputStream = new ObjectOutputStream(client.getOutputStream());

        objectInputStream = new ObjectInputStream(client.getInputStream());

        objectOutputStream.flush();

        run();

        finishTransfer();

    }

    //Ceci permet de deconnecter le client comme le veut l'enonce
    public static void finishTransfer() throws IOException {
        client.close();
        ClientLauncher.closeSocket();

        objectInputStream.close();
        objectOutputStream.close();
    }

    // Permet l'inscription du GUI
    public static String startGUI(ArrayList entry){
        try {

            String com="";  // sera null; juste pour permettre une methode pour le CLI/GUI

            Boolean done = true;  //signifie GUI = true; et par la meme occasion CLI=false

            com="INSCRIRE";

            justNeedSocket();  //ouverture de la connection

            inscrire(true, com, done, entry);  //Entrees

            return GUIMessage();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

   //permet de faire les entrees pour charger les cours (utilisation plutot CLI)
    public static void run() {

        boolean go=true;

        while (go==true) {

            try {

                Scanner scanner = new Scanner(System.in);

                String com = "";
                Boolean done = true;

                System.out.println("*** Bienvenue au portail de cours de l'UDEM *** \n"
                        + "Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours: \n"
                        + "1: Automne \n" + "2: Hiver \n" + "3: Ete");

                System.out.print("> Choix: ");

                com = scanner.nextLine();

                //Possibilites de 1 ou 2 ou 3 uniquement; validation ici
                Pattern choix1 = Pattern.compile("[1-3]");
                Matcher choixSession = choix1.matcher(com);
                boolean validSession = choixSession.find();
                if (!validSession) {
                    throw new IllegalArgumentException("Erreur. Session invalide.");
                }

                if (com.equals("1") || com.equals("2") || com.equals("3")) {
                    try {
                        objectOutputStream.writeObject("CHARGER " + com);
                    } catch (IOException e) {
                        // Handle the exception
                        System.out.println(com);
                        e.printStackTrace();
                    }

                    objectOutputStream.flush();

                    ArrayList<Course> leCours =  (ArrayList<Course>) objectInputStream.readObject();

                    setCourseEntry(leCours);

                    System.out.println(leCours);

                    System.out.println("1: Consulter les cours offerts pour une autre session \n" +
                            "2: Inscription à un cours \n");

                    System.out.print("> Choix :");
                    com= scanner.nextLine();

                    //Possibilites de 1 ou 2 uniquement; validation ici
                    Pattern choix2 = Pattern.compile("[1-2]");
                    Matcher choixInscription = choix2.matcher(com);
                    boolean validInscription = choixInscription.find();
                    if (!validInscription) {
                        throw new IllegalArgumentException("Erreur. Choix invalide.");
                    }

                    // On veut retourner au menu precedent ici; donc choisir la session a nouveau
                    if (com.equals("1")) {
                        com = "1";
                        go= true;
                        finishTransfer();
                        prepTransfer();
                    }

                    //On veut passer a l'inscription
                    else if (com.equals("2")) {
                        com = "2";
                        go = false;
                    }


                }

                //INSCRIPTION; on appelle les methodes requises
                if (com.equals("2")) {

                    com = "INSCRIRE";

                    ArrayList<String> entry = new ArrayList<>(); //Utilisation pour le GUI; mais ici il sera null

                    inscrire(false,   com, done, entry);

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
    public static ArrayList<Course> charger1( String com) throws IOException, ClassNotFoundException {


        // on cree la connection
        justNeedSocket();

        try {
            objectOutputStream.writeObject("CHARGER "+com);
        } catch (IOException e) {
            // Handle the exception
            System.out.println("CHARGER");
            e.printStackTrace();
        }

        objectOutputStream.flush();

        ArrayList<Course> Cours =  (ArrayList<Course>) objectInputStream.readObject();

        setCourseEntry(Cours);

        finishTransfer();

        return arraylist();


    }


    //methode qui permet inscriptin CLI et GUI
    public static void inscrire(Boolean dev3,   String com, Boolean done, ArrayList entry) throws IOException {

        try {
            objectOutputStream.writeObject("INSCRIRE");
        } catch (IOException e) {
            // Handle the exception
            System.out.println("probleme avec oos");
            e.printStackTrace();
        }

        objectOutputStream.flush();

        while (done) {

            if (dev3!=true) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Veuillez saisir votre prénom: ");
                String firstName = scanner.nextLine();

                //Validations
                Pattern nums = Pattern.compile("\\d|^$");
                Matcher prenom = nums.matcher(firstName);
                boolean numsInPrenom = prenom.find();
                if (numsInPrenom) {
                    throw new IllegalArgumentException("Erreur. Prénom invalide.");
                }


                System.out.println("Veuillez saisir votre nom: ");
                String lastName = scanner.nextLine();

                //Validations
                Matcher nom = nums.matcher(lastName);
                boolean numsInNom = nom.find();
                if (numsInNom) {
                    throw new IllegalArgumentException("Erreur. Nom invalide.");
                }


                System.out.println("Veuillez saisir votre email: ");
                String email = scanner.nextLine();

                //Validations
                Pattern mail = Pattern.compile(".+[^.]@.+[.][a-z][a-z][a-z]?$");
                Matcher courriel = mail.matcher(email);
                boolean correctEmail = courriel.find();
                if (!correctEmail) {
                    throw new IllegalArgumentException("Erreur. E-mail invalide.");
                }


                System.out.println("Veuillez saisir votre matricule: ");
                String matricule = scanner.nextLine();

                //Validations
                Pattern mat = Pattern.compile("[0-9]{8}");
                Matcher matriculeEtu = mat.matcher(matricule);
                boolean correctMatricule = matriculeEtu.find();
                if (!correctMatricule) {
                    throw new IllegalArgumentException("Erreur. Matricule invalide.");
                }


                System.out.println("Veuillez saisir le code du cours: ");
                String courseName = scanner.nextLine();

                //Validations
                Pattern code = Pattern.compile("^[a-zA-Z]{3}[0-9]{4}$");
                Matcher codeCours = code.matcher(courseName);
                boolean correctCode = codeCours.find();
                if (!correctCode) {
                    throw new IllegalArgumentException("Erreur. Le code de cours entré est invalide.");
                }


                ArrayList<Course> courseEn =arraylist();


                int size = courseEn.size();
                Course courInfo = new Course("", "", "");
                for (int i = 0; i < size; i++) {

                    if (courseEn.get(i).getCode().equals(courseName)) {
                        courInfo = courseEn.get(i);
                    }

                }
                if (courInfo.getName() != "") {

                    RegistrationForm userRegistration = new RegistrationForm(firstName, lastName, email, matricule, courInfo);

                    justNeedSocket();
                    objectOutputStream.writeObject(userRegistration);
                    objectOutputStream.flush();

                    String msg = ("Félicitations! Inscription réussie de " + userRegistration.getPrenom() + " au cours " + userRegistration.getCourse().getCode());
                    System.out.println(msg);

                    finishTransfer();

                } else {
                    System.out.println("Ce cours n'est malheureusement pas disponible.");

                    justNeedSocket();
                    objectOutputStream.writeObject("ERROR");
                    objectOutputStream.flush();
                    finishTransfer();

                }
            }else {
                message = "";
                justNeedSocket();

                String prenomGUI = entry.get(0).toString();
                String nomGUI = entry.get(1).toString();
                String emailGUI = entry.get(2).toString();

                //Validations
                Pattern mail = Pattern.compile(".+[^.]@.+[.][a-z][a-z][a-z]?$");
                Matcher courriel = mail.matcher(emailGUI);
                boolean correctEmail = courriel.find();

                //Message boite d'erreurs
                if (correctEmail != true) {
                    message = "Courriel invalide";
                }


                String matriculeGUI = entry.get(3).toString();

                //Validations
                Pattern mat = Pattern.compile("[0-9]{8}");
                Matcher matriculeEtu = mat.matcher(matriculeGUI);
                boolean correctMatricule = matriculeEtu.find();

                //Message boite d'erreurs
                if (correctMatricule ==false) {
                    if (message == "") {
                        message = "Matricule invalide";
                    } else {
                        message = "Courriel et Matricule Invalide";
                    }
                }

                if (message == "") {
                    String courGUI = entry.get(4).toString();

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
                        objectOutputStream.writeObject(userRegistration);
                        objectOutputStream.flush();

                        String msg = ("Félicitations! Inscription réussie de " + userRegistration.getPrenom() + " au cours " + userRegistration.getCourse().getCode());

                        message = (msg);

                        finishTransfer();

                    } else {

                        message = ("");

                        objectOutputStream.writeObject("ERROR");
                        objectOutputStream.flush();

                        finishTransfer();
                    }

                }else {
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

