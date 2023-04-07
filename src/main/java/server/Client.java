package server;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){

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
            System.out.println("What: ");
            Boolean  done=true;
            String com = scanner.nextLine();

            while(done && com.equals("INSCRIRE")){

            try {
                objectOutputStream.writeObject(com);
            } catch (IOException e) {
                // Handle the exception
                System.out.println(com);
                e.printStackTrace();
            }


            objectOutputStream.flush();




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

                Course cour1= new Course("Programmation1","IFT1015","Automne");
                Course cour2= new Course("Base_de_donnees","IFT2256","Hiver");
                Course cour3= new Course("Architecture_des_ordinateurs","IFT1227","Ete");
                ArrayList<Course> course = new ArrayList<>();

                course.add(cour1);
                course.add(cour2);
                course.add(cour3);
                int size = course.size();
                Course courInfo=new Course ("","","");
           for (int i=0; i<size;i++){

              if (course.get(i).getCode().equals(courseName)){
                courInfo=course.get(i);
              }

          }
           if (courInfo.getName()!="") {
               RegistrationForm userRegistration = new RegistrationForm(firstName, lastName, email, matricule, courInfo);
               objectOutputStream.writeObject(userRegistration);
               objectOutputStream.flush();

               String msg=("Félicitations! Inscription réussie de " + userRegistration.getPrenom()+" au cours " + userRegistration.getCourse().getCode() );
               System.out.println(msg);

           }
           else{
               System.out.println( "Unfortunately this course is not available");
               objectOutputStream.writeObject("ERROR");
               objectOutputStream.flush();
           }




            done=false;

             writer.close();
        }

            objectOutputStream.close();

            socket.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

