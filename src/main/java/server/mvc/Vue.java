package server.mvc;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.geometry.Pos;
import server.models.Course;

import java.util.ArrayList;

/**
 * Dans cette classe nous definissons les éléments graphiques de notre
 * application.
 * Elle sera modifiee en utilisant Controller.java qui utilisera
 * Modele.java pour effectuer les operations.
 */
public class Vue extends BorderPane {
    private ObservableList<Course> courses;   //Pour le TableView
    private ArrayList<Course> entries;   //Pour stocker les donnees a envoyer pour l'inscription

    private Button charger = new Button("Charger");
    private Button envoyer = new Button("Envoyer");
    private Button buttonAlert = new Button("Cancel");

    private TextField prenom = new TextField("");
    private TextField nom = new TextField("");
    private TextField email = new TextField("");
    private TextField matricule = new TextField("");
    private final Text textValeur1 = new Text("Liste des Cours");
    private final Text textValeur2 = new Text("Formulaire d'inscription");
    private ComboBox combobox; //liste deroulante pour choisir la session


    private TableView table1; //table ou seront presentes les cours

    /**
     * R&eacute;cup&egrave;re le tableau avec tous les &eacute;l&eacute;ments graphiques comme les boites et les champs
     * de saisie
     * @return Retourne le tableau avec tous les &eacute;l&eacute;ments grpahiques comme les boites et les champs
     * de saisie
     */
    public TableView getTable (){
        return table1;
    }

    /**
     * D&eacute;finit la valeur du tableau avec tous les &eacute;l&eacute;ments grpahiques comme les boites
     * et les champs de saisie
     * @param table tableau avec tous les &eacute;l&eacute;ments grpahiques comme les boites et les champs de saisie
     */
    public void setTable(TableView table) {
        this.table1 = table;
    }

    //On a besoin d'envoyer des chiffres au serveur; on fait la conversion ici

    /**
     * Traduit le nom des sessions par leur chiffre attribué pour les envoyer au serveur
     * @return Retourne le chiffre correspondant à la session sélectionnée par l'utilisateur
     */
    public String getSession(){
        String re = "";
        if (combobox.getValue()!=null) {
            String com = combobox.getValue().toString();


            switch (com) {
                case "Automne":
                    re = "1";
                    break;
                case "Hiver":
                    re = "2";
                    break;
                case "Ete":
                    re = "3";
                    break;

                default:
                    break;
            }
        }
        return re;
    }

    /**
     * D&eacute;finit le menu déroulant qui permet de choisir la session
     * @param combobox Menu déroulant qui permet de choisir la session
     */
    public void setCombobox(ComboBox combobox) {
        this.combobox = combobox;
    }


    /**
     * Crée les tableaux, boites, champs et zones de l'interface graphique
     */
    public Vue() {
        TableView table= new TableView();

        BorderPane borderPane = new BorderPane();
        prenom.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: black; -fx-background-color: #f4f4f4;");
        nom.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: black; -fx-background-color: #f4f4f4;");
        email.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: black; -fx-background-color: #f4f4f4;");
        matricule.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: black; -fx-background-color: #f4f4f4;");

        prenom.setPrefWidth(200);
        nom.setPrefWidth(200);
        email.setPrefWidth(200);
        matricule.setPrefWidth(200);

        prenom.setPrefHeight(30);
        nom.setPrefHeight(30);
        email.setPrefHeight(30);
        matricule.setPrefHeight(30);

        textValeur2.setFont(Font.font("Arial", 20));
        textValeur1.setFont(Font.font("Arial", 20));


        Label label1 = new Label("Prénom     ");
        Label label2 = new Label("Nom           ");
        Label label3 = new Label("Email          ");
        Label label4 = new Label("Matricule  ");
        Label label5 = new Label("Cours         ");


        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.5f); // On veut la separation entre les 2 sections comme sur la photo

        // La section gauche avec bouton charger et la table
        VBox leftSection = new VBox();

        String session[] =
                { "Automne", "Hiver", "Ete"};
        ComboBox combo_box =
                new ComboBox(FXCollections
                        .observableArrayList(session));
        combo_box.setMinWidth(80);

        table.setEditable(true);

        setCombobox(combo_box);

        // Initialisation de la table
        table = new TableView<Course>();
        TableColumn<Course, String> codeCol = new TableColumn<Course, String>("Code");
        TableColumn<Course, String> coursCol = new TableColumn<Course, String>("Cours");
        table.getColumns().addAll(codeCol, coursCol);


        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        coursCol.setCellValueFactory(new PropertyValueFactory<>("name"));


        codeCol.setMinWidth(170);
        coursCol.setMinWidth(170);
        codeCol.setMaxWidth(170);
        coursCol.setMaxWidth(170);

        // On insere les elements du cote gauche
        HBox but_left = new HBox(100,combo_box,charger); //pour la liste et le bouton cote-a-cote
        VBox left= new VBox(20,textValeur1,table, but_left); //pour la table ainsi que (la liste et le bouton) alignes verticalement
        left.setAlignment(Pos.CENTER);
        textValeur1.setFont(Font.font("Arial", 20));
        leftSection.getChildren().addAll(
                left
        );



        // Section droite
        VBox rightSection = new VBox();
        //Ajout des elements du cote droit
        HBox textBoxes2 = new HBox(new HBox(15, label2, nom));
        HBox textBoxes1 = new HBox(new HBox(15, label1, prenom));
        HBox textBoxes3 = new HBox(new HBox(15, label3, email));
        HBox textBoxes4 = new HBox(new HBox(15, label4, matricule));

        textBoxes1.setPadding(new Insets(10, 10, 10, 10));
        textBoxes2.setPadding(new Insets(10, 10, 10, 10));
        textBoxes3.setPadding(new Insets(10, 10, 10, 10));
        textBoxes4.setPadding(new Insets(10, 10, 10, 10));

        // l'utilisation de i, j est juste pour centrer le bouton et le texte
        VBox i = new VBox(10,textValeur2);
        i.setAlignment(Pos.CENTER);
        VBox j = new VBox(10, envoyer);
        j.setAlignment(Pos.CENTER);

        rightSection.getChildren().addAll(
                i,
                textBoxes1,
                textBoxes2,
                textBoxes3,
                textBoxes4,
                j
        );



        leftSection.setStyle("-fx-background-color: #e3e1cd;");
        leftSection.setPadding(new Insets(40, 30, 10, 40));
        rightSection.setStyle("-fx-background-color: #e3e1cd;");
        rightSection.setPadding(new Insets(40, 30, 10, 40));

        //On ajoute tous les elements desormais
        splitPane.getItems().addAll(leftSection, rightSection);

        //Centrer le tout
        this.setCenter(splitPane);
        setTable((table));

    }

    /**
     * Récupère la valeur du bouton "charger"
     * @return Retourne la valeur du bouton "charger"
     */
    public Button getChargerButton() {
        return this.charger;
    }

    /**
     * Récupère la valeur du texte en haut du champ d'ecriture "pr&eacute;nom"
     * @return Retourne la valeur du texte en haut du champ d'écriture "prénom"
     */
    public TextField getprenom() {
        return this.prenom;
    }

    /**
     * D&eacute;finit la valeur de la liste de cours disponibles
     * @param entries La liste de cours disponibles
     */
    public void setEntries(ArrayList<Course> entries) {
        this.entries = entries;
    }

    /**
     * R&eacute;cup&egrave;re la valeur de la liste de cours disponibles
     * @return Retourne la valeur de la liste de cours disponibles
     */
    public ArrayList<Course> getEntries() {
        return entries;
    }

    /**
     * R&eacute;cup&egrave;re la valeur du nom
     * @return Retourne la valeur du nom
     */
    public TextField getnom() {
        return this.nom;
    }

    /**
     * R&eacute;cup&egrave;re la valeur de l'email
     * @return Retourne la valeur de l'email
     */
    public TextField getemail() {
        return this.email;
    }

    /**
     * R&eacute;cup&egrave;re la valeur du matricule
     * @return Retourne la valeur du matricule
     */
    public TextField getmatricule() {
        return this.matricule;
    }

    /**
     * R&eacute;cup&egrave;re la valeur du bouton "envoyer"
     * @return Retourne la valeur du bouton "envoyer"
     */
    public Button getEnvoyerButton() {
        return this.envoyer;

    }

    /**
     * Permet d'afficher les messages d'erreur sur l'interface graphique
     * @param errorMessage Message d'erreur a afficher
     */
    public void displayError(String errorMessage) {

        //Message d'erreur; missing data par eg
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }


    /**
     * Permet d'afficher les details sur l'erreur affich&eacute;e
     * @param infoError Details sur l'erreur affich&eacute;e
     */
    public void displayInfoError(String infoError) {

        //Message d'inscription

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Message");
        alert.setContentText(infoError);
        alert.showAndWait();


    }

    /**
     * Affiche les cours de la session s&eacute;lectionn&eacute;e
     * @param entries la liste des cours de la session s&eacute;lectionn&eacute;e
     * @throws IndexOutOfBoundsException Si l'affichage &eacute;ssaye de prendre un cours apr&egrave;s le dernier
     * &eacute;l&eacute;ment de la liste de cours
     */
    public void displayCours(ArrayList<Course> entries) throws IndexOutOfBoundsException{
        courses = FXCollections.observableArrayList();
        int i=0;

        Course selectedCourse = (Course) getTable().getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            //Enlever les elements precedent de la liste pour inserer ceux de la nouvelle session
            getTable().getItems().remove(selectedCourse);
        }

        TableView tab = getTable();


        tab.getItems().clear();

        //Ajout des cours de la session choisie
        if (entries.get(0)!=null) {
            for (Course co : entries) {
                tab.getItems().add(co);
            }
        }


        setTable(tab);

    }

}
