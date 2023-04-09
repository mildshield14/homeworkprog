package server;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
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


    public static void main(String[] args) {

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

                System.out.println("What: ");
                com = scanner.nextLine();

            if (com.equals("INSCRIRE")) {
ArrayList<String> entry= new ArrayList<>();
               inscrire(false,writer, socket, scanner, objectOutputStream, com, done, entry);

            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void inscrire(Boolean dev3, BufferedWriter writer, Socket socket, Scanner scanner, ObjectOutputStream objectOutputStream, String com, Boolean done, ArrayList entry) throws IOException {

        try {
            objectOutputStream.writeObject(com);
        } catch (IOException e) {
            // Handle the exception
            System.out.println(com);
            e.printStackTrace();
        }


        objectOutputStream.flush();

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

                //Test Course

                Course cour1 = new Course("Programmation1", "IFT1015", "Automne");
                Course cour2 = new Course("Base_de_donnees", "IFT2256", "Hiver");
                Course cour3 = new Course("Architecture_des_ordinateurs", "IFT1227", "Ete");
                ArrayList<Course> course = new ArrayList<>();

                course.add(cour1);
                course.add(cour2);
                course.add(cour3);
                int size = course.size();
                Course courInfo = new Course("", "", "");
                for (int i = 0; i < size; i++) {

                    if (course.get(i).getCode().equals(courseName)) {
                        courInfo = course.get(i);
                    }

                }
                if (courInfo.getName() != "") {
                    RegistrationForm userRegistration = new RegistrationForm(firstName, lastName, email, matricule, courInfo);
                    objectOutputStream.writeObject(userRegistration);
                    objectOutputStream.flush();

                    String msg = ("Félicitations! Inscription réussie de " + userRegistration.getPrenom() + " au cours " + userRegistration.getCourse().getCode());
                    System.out.println(msg);

                } else {
                    System.out.println("Unfortunately this course is not available");
                    objectOutputStream.writeObject("ERROR");
                    objectOutputStream.flush();
                }

            }else{
                String prenomGUI=entry.get(0).toString();
                String nomGUI=entry.get(1).toString();
                String emailGUI=entry.get(2).toString();
                String matriculeGUI=entry.get(3).toString();
                String courGUI=entry.get(4).toString();

                System.out.println(courGUI);


                //Test Course

                Course cour1 = new Course("Programmation1", "IFT1015", "Automne");
                Course cour2 = new Course("Base_de_donnees", "IFT2256", "Hiver");
                Course cour3 = new Course("Architecture_des_ordinateurs", "IFT1227", "Ete");
                ArrayList<Course> course = new ArrayList<>();

                course.add(cour1);
                course.add(cour2);
                course.add(cour3);
                int size = course.size();
                Course courInfo = new Course("", "", "");
                for (int i = 0; i < size; i++) {

                    if (course.get(i).getCode().equals(courGUI)) {
                        courInfo = course.get(i);
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


