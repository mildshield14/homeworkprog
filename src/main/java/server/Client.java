package server;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
public class Client {
    public static ArrayList<Course> courseEntry;
    public static String message;
    public static String startGUI(ArrayList entry){
        try {

            int port = 1337;

            // create a socket on the specified port
            Socket socket = new Socket("localhost", port);


            System.out.println("Connected to server on port " + port);
            OutputStreamWriter os = new OutputStreamWriter(
                    socket.getOutputStream()
            );


            BufferedWriter writer = new BufferedWriter(os);

            Scanner scanner = new Scanner(System.in);


            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            String com="";
            Boolean done = true;

                com="INSCRIRE";


                inscrire(true,writer, socket, scanner, objectOutputStream, com, done, entry);

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

    public static void setCourseEntry(ArrayList<Course> courseEntry) {
        Client.courseEntry = courseEntry;
    }

    public static void main(String[] args) {

        boolean go=true;

        while (go==true) {
            try {

                int port = 1337;

                // create a socket on the specified port
                Socket socket = new Socket("localhost", port);


                System.out.println("Connected to server on port " + port);
                OutputStreamWriter os = new OutputStreamWriter(
                        socket.getOutputStream()
                );


                BufferedWriter writer = new BufferedWriter(os);

                Scanner scanner = new Scanner(System.in);


                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                String com = "";
                Boolean done = true;

                 System.out.println("Entrez le numéro correspondant à la session désirée:");

                com = scanner.nextLine();
                if (com.equals("1") || com.equals("2") || com.equals("3")) {
                    try {
                        objectOutputStream.writeObject("CHARGER " + com);
                    } catch (IOException e) {
                        // Handle the exception
                        System.out.println(com);
                        e.printStackTrace();
                    }

                    objectOutputStream.flush();

                    System.out.println("1: Consulter les cours offerts pour une autre session \n" +
                            "2: Inscription à un cours \n");
                    com = scanner.nextLine();

                    if (com.equals("1")) {
                        com = "1";
                        go= true;
                    }

                    else if (com.equals("2")) {
                        com = "2";
                        go = false;
                    }

                }
                
                FileInputStream fis = new FileInputStream("src/main/java/server/data/courses.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
                Object ListeDeCours = ois.readObject();
                ois.close();
                Courses.add(ListeDeCours);
                System.out.println(ListeDeCours);

                ArrayList<Course> Cours = new ArrayList<Course>();
                ArrayList<String> leCours = (ArrayList) Courses.get(0);

                for (int i=0; i<leCours.size(); i++) {
                    String aCourse = (String) leCours.get(i);
                    String[] words = aCourse.split("\\s+");
                    ArrayList<String> separations = new ArrayList<String>();
                    for (String word : words) {
                        separations.add(word);
                            }
                    Cours.add(new Course(separations.get(1), separations.get(0), separations.get(2)));
                    }
                    System.out.println(Cours);


//Test Course

                /*Course cour1 = new Course("Programmation1", "IFT1015", "Automne");
                Course cour2 = new Course("Base_de_donnees", "IFT2256", "Hiver");
                Course cour3 = new Course("Architecture_des_ordinateurs", "IFT1227", "Ete");
                ArrayList<Course> courseEn = new ArrayList<>();


                courseEn.add(cour1);
                courseEn.add(cour2);
                courseEn.add(cour3);*/
                setCourseEntry(Cours);

                if (com.equals("2")) {
                    com = "INSCRIRE";
                    ArrayList<String> entry = new ArrayList<>(); //Utilisation pour le GUI; mais ici il sera null
                    inscrire(false, writer, socket, scanner, objectOutputStream, com, done, entry);

                }

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void inscrire(Boolean dev3, BufferedWriter writer, Socket socket, Scanner scanner, ObjectOutputStream objectOutputStream, String com, Boolean done, ArrayList entry) throws IOException {
        int port1 = 1337;

        // create a socket on the specified port
        Socket socket1 = new Socket("localhost", port1);


        System.out.println("Connected to server on port " + port1);


        OutputStreamWriter os = new OutputStreamWriter(
                socket1.getOutputStream()
        );



        ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(socket1.getOutputStream());


        try {
            objectOutputStream1.writeObject("INSCRIRE");
        } catch (IOException e) {
            // Handle the exception
            System.out.println("INSCRIRE");
            e.printStackTrace();
        }


        objectOutputStream1.flush();

        while (done) {

            if (dev3!=true) {

                System.out.println("Enter your first name: ");
                String firstName = scanner.nextLine();

                System.out.println("Enter your last name: ");
                String lastName = scanner.nextLine();

                System.out.println("Enter your email: ");
                String email = scanner.nextLine();

                System.out.println("Enter your matricule: ");
                String matricule = scanner.nextLine();

                System.out.println("Enter your course name: ");
                String courseName = scanner.nextLine();


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
                    objectOutputStream1.writeObject(userRegistration);
                    objectOutputStream1.flush();

                    String msg = ("Félicitations! Inscription réussie de " + userRegistration.getPrenom() + " au cours " + userRegistration.getCourse().getCode());
                    System.out.println(msg);

                } else {
                    System.out.println("Unfortunately this course is not available");
                    objectOutputStream1.writeObject("ERROR");
                    objectOutputStream1.flush();
                }

            }else{
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

                } else {
                    message=("");
                    System.out.println("Unfortunately this course is not available");
                    objectOutputStream.writeObject("ERROR");
                    objectOutputStream.flush();
                }

            }


                done = false;

            writer.close();
        }

        objectOutputStream.close();

        socket.close();
    }
}


