package server.mvc;

import javafx.scene.control.TextField;
import server.Client;
import server.models.Course;

import java.io.IOException;
import java.util.ArrayList;

public class Model{

private TextField value1;

private ArrayList<Course> entries;
private TextField value2;
private TextField value3;
private TextField value4;

private String code;
private String errorMessage;
private String infoError;
public Model(){
        value1 = new TextField();
        value2 = new TextField();
        value3 = new TextField();
        value4 = new TextField();
        code= "";
        }

public String getValue1() {
        return value1.getText();
        }

        public String getCode() {
                return code;
        }

        public void setCode(String code) {
                this.code = code;
        }

        public void setValue1(String value1) {
        this.value1.setText(value1);
        }

        public void setEntries(ArrayList<Course> entries) {
                this.entries = entries;
        }

        public ArrayList<Course> getEntries() {
                return entries;
        }

        public String getValue2() {
        return value2.getText();
        }



public void setValue2(String value2) {
        this.value2.setText(value2);
        }

public String getValue3() {
        return value3.getText();
        }

public void setValue3(String value3) {
        this.value3.setText(value3);
        }

public String getValue4() {
        return value4.getText();
        }

public void setValue4(String value4) {
        this.value4.setText(value4);
        }


public void updateData(String values1, String values2, String values3, String values4, String code) {
        setValue1(values1);
        setValue2(values2);
        setValue3(values3);
        setValue4(values4);
        setCode(code);
        }

public void setError(String errorMessage) {
                this.errorMessage = errorMessage;
        }
public String getError() {
                return this.errorMessage;
        }
public String getInfoError(){
        return this.infoError;
        }
public void setInfoError(String infoError){
        this.infoError=infoError;
        }
        
public void charger(String com) throws IOException, ClassNotFoundException {


        ArrayList<Course> en;
        en=Client.charger1(com);
        setEntries(en);

}
public void envoyer() {
        boolean b = true;

        String prenom= String.valueOf((getValue1()));
        String nom= String.valueOf((getValue2()));
        String email= String.valueOf((getValue3()));
        String matricule= String.valueOf((getValue4()));

        String k=prenom;
        setInfoError("");
        if (!(k .equals("") || nom.equals("") || email.equals("") || matricule.equals("")|| code.equals(""))){
                ArrayList<String> entries = new ArrayList<>();
                        setError("");
                entries.add(prenom);
                entries.add(nom);
                entries.add(email);
                entries.add(matricule);
                entries.add(getCode());
        
                Client.startGUI(entries);
        
                setInfoError(Client.GUIMessage());

        }

        else{

          setError("Text boxes cannot be empty");

        }
        
    }



}

