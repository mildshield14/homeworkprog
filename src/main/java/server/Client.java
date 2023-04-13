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

public class Client {
    public static ArrayList<Course> courseEntry;
    public static String message;
    public static Boolean ins;
    public static Socket client;

    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

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

    public static void finishTransfer() throws IOException {
        client.close();
        ClientLauncher.closeSocket();
        System.out.println("FINISH");
        objectInputStream.close();

        objectOutputStream.close();
    }
    public static String startGUI(ArrayList entry){
        try {

            Scanner scanner = new Scanner(System.in); //to pass to procedure// will be null

            String com="";
            Boolean done = true;

            com="INSCRIRE";
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
    public static String GUIMessage(){
        return message;
    }


    public static ArrayList arraylist (){return courseEntry;}

    public static void setCourseEntry(ArrayList<Course> courseEntry1) {
        courseEntry = courseEntry1;
    }


    public Client(int port) throws IOException {
        ClientLauncher.setConnection();

        this.client = ClientLauncher.getSocket();


    }

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

                    System.out.println(leCours+"IS IT ?");

                    System.out.println("CHECKING");

                    setCourseEntry(leCours);



                    System.out.println(leCours);

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
                        go= true;
                        finishTransfer();
                        prepTransfer();
                    }

                    else if (com.equals("2")) {
                        com = "2";
                        go = false;
                    }
                    System.out.println(leCours);

                }




                if (com.equals("2")) {
                    com = "INSCRIRE";
                    System.out.println("DOES IT GO HERE");
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


        // create a socket on the specified port+ object out and in

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

        System.out.println(Cours);
        finishTransfer();
        return arraylist();


    }

    public static void inscrire(Boolean dev3,   String com, Boolean done, ArrayList entry) throws IOException {
        ins=false;
        System.out.println("ANd here?");

        try {
            objectOutputStream.writeObject("INSCRIRE");
        } catch (IOException e) {
            // Handle the exception
            System.out.println("INSCRIRE");
            e.printStackTrace();
        }


        objectOutputStream.flush();

        while (done) {

            if (dev3!=true) {
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
                Pattern mat = Pattern.compile("[0-9]{8}");
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
                    System.out.println("HI");
                    justNeedSocket();
                    System.out.println("CHECKING");
                    objectOutputStream.writeObject(userRegistration);
                    System.out.println("YO");
                    objectOutputStream.flush();
                    finishTransfer();
                    System.out.println("HELLO");
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
            }else{
                justNeedSocket();
                String prenomGUI=entry.get(0).toString();
                String nomGUI=entry.get(1).toString();
                String emailGUI=entry.get(2).toString();
                String matriculeGUI=entry.get(3).toString();
                String courGUI=entry.get(4).toString();

                System.out.println(courGUI);


                ArrayList<Course> courseEn =arraylist();

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

                    message=(msg);
                    System.out.println(msg);
                    finishTransfer();
                } else {
                    message=("");
                    System.out.println("Ce cours n'est malheureusement pas disponible.");
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
