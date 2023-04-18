package server.mvc;

import javafx.scene.control.TextField;
import server.Client;
import server.models.Course;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Contient la logique du fonctionnement de l'application
 */
public class Model{

        private TextField value1;

        private ArrayList<Course> entries;
        private TextField value2;
        private TextField value3;
        private TextField value4;

        private String code;
        private String errorMessage;
        private String infoError;

        /**
         * Valeurs des champs à remplir par l'utilisateur
         */
        public Model(){
                value1 = new TextField();
                value2 = new TextField();
                value3 = new TextField();
                value4 = new TextField();
                code= "";
        }

        /**
         * R&eacute;cup&eacute;ration du prénom
         * @return renvoie le prénom
         */
        public String getValue1() {
                return value1.getText();
        }

        /**
         * R&eacute;cup&egrave;re le texte entré pour le code du cours
         * @return Retourne le texte entré pour le code du cours
         */
        public String getCode() {
                return code;
        }

        /**
         * D&eacute;finit le code du cours
         * @param code code du cours
         */

        public void setCode(String code) {
                this.code = code;
        }

        /**
         * D&eacute;finit le prenom
         * @param value1 prenom
         */
        public void setValue1(String value1) {
                this.value1.setText(value1);
        }

        /**
         * D&eacute;finit la liste de cours à charger
         * @param entries Liste de cours à charger
         */
        public void setEntries(ArrayList<Course> entries) {
                this.entries = entries;
        }

        /**
         * R&eacute;cup&egrave;re la liste de cours à charger
         * @return Retourne la liste de cours à charger
         */
        public ArrayList<Course> getEntries() {
                return entries;
        }

        /**
         * R&eacute;cup&egrave;re le nom
         * @return Retourne la valeur du nom
         */
        public String getValue2() {
                return value2.getText();
        }


        /**
         * D&eacute;finit le nom
         * @param value2 le nom
         */
        public void setValue2(String value2) {
                this.value2.setText(value2);
        }

        /**
         * R&eacute;cupre l'email
         * @return Retourne l'email
         */
        public String getValue3() {
                return value3.getText();
        }

        /**
         * D&eacute;finit l'email
         * @param value3 Email
         */
        public void setValue3(String value3) {
                this.value3.setText(value3);
        }

        /**
         * R&eacute;cup&egrave;re le cours entr&eacute;e par l'utilisateur
         * @return Retourne le cours entr&eacute; par l'utilisateur
         */
        public String getValue4() {
                return value4.getText();
        }

        /**
         * D&eacute;finit le code de cours entré par l'utilisateur
         * @param value4 le code de cours entré par l'utilisateur
         */
        public void setValue4(String value4) {
                this.value4.setText(value4);
        }


        /**
         * Associe ce qu'a saisi l'utilisateur à
         * @param values1 prénom
         * @param values2 nom
         * @param values3 Email
         * @param values4 Cours
         * @param code Code du cours
         */
        public void updateData(String values1, String values2, String values3, String values4, String code) {
                setValue1(values1);
                setValue2(values2);
                setValue3(values3);
                setValue4(values4);
                setCode(code);
        }

        /**
         * D&eacute;finit le message d'erreur de l'interface graphique
         * @param errorMessage le message d'erreur correspondant à la situation
         */
        public void setError(String errorMessage) {
                this.errorMessage = errorMessage;
        }

        /**
         * R&eacute;p&egrave;re le message d'erreur
         * @return Retourne le message d'erreur
         */
        public String getError() {
                return this.errorMessage;
        }

        /**
         * R&eacute;cup&egrave;re les details sur le message d'erreur
         * @return Retourne les details sur le message d'erreur
         */
        public String getInfoError(){
                return this.infoError;
        }

        /**
         * D&eacute;finit les details de l'erreur
         * @param infoError details sur l'erreur
         */
        public void setInfoError(String infoError){
                this.infoError=infoError;
        }

        /**
         * Recoit la liste de cours
         * @param com session demand&eacute;e
         * @throws IOException Si charger1 rencontre un probl&egrave;me lors de la r&eacute;cup&eacute;ration de
         * la liste de cours
         * @throws ClassNotFoundException Si charger1 est introuvable
         */
        public void charger(String com) throws IOException, ClassNotFoundException {


                ArrayList<Course> en;
                en=Client.charger1(com);
                setEntries(en);

        }

        /**
         * Envoie les informations entr&egrave;es au client
         */
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
