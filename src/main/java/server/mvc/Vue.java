package server;
package mvc;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.geometry.Pos;

/*
 * Dans cette classe nous definissons les éléments graphiques de notre
 * application.
 * Notez que cette classe est completement independante de toute definition
 * de comportement.
 */
public class Vue extends BorderPane {

    private Button charger = new Button("Charger");
    private Button envoyer = new Button("Envoyer");
    private Button buttonAlert = new Button("Cancel");

    private TextField textBox1 = new TextField("");
    private TextField textBox2 = new TextField("");
    private TextField textBox3 = new TextField("");
    private TextField textBox4 = new TextField("");
    private TextField textBox5 = new TextField("");
    private Text textValeur1 = new Text("Liste des Cours");
    private Text textValeur2 = new Text("Formulaire d'inscription");


    public Vue() {
        this.setStyle("-fx-background-color: #e3e1cd;");
        BorderPane borderPane = new BorderPane();
        textBox1.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: black; -fx-background-color: #f4f4f4;");
        textBox2.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: black; -fx-background-color: #f4f4f4;");
        textBox3.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: black; -fx-background-color: #f4f4f4;");
        textBox4.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: black; -fx-background-color: #f4f4f4;");
        textBox5.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: black; -fx-background-color: #f4f4f4;");

        textBox1.setPrefWidth(160);
        textBox2.setPrefWidth(160);
        textBox3.setPrefWidth(160);
        textBox4.setPrefWidth(160);
        textBox5.setPrefWidth(160);

        textBox1.setPrefHeight(30);
        textBox2.setPrefHeight(30);
        textBox3.setPrefHeight(30);
        textBox4.setPrefHeight(30);
        textBox5.setPrefHeight(30);

        textValeur2.setFont(Font.font("Arial", 20));
        textValeur1.setFont(Font.font("Arial", 20));


        Label label1 = new Label("Prénom     ");
        Label label2 = new Label("Nom           ");
        Label label3 = new Label("Email          ");
        Label label4 = new Label("Matricule  ");
        Label label5 = new Label("Cours         ");


        // Create the split pane
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.5f); // Set the divider position to split the pane in half

        // Create the left section
        VBox leftSection = new VBox();

        textValeur1.setFont(Font.font("Arial", 20));
        leftSection.getChildren().addAll(
                textValeur1,
                charger
        );

        // Create the right section
        VBox rightSection = new VBox();
        envoyer.setAlignment(Pos.CENTER);
        // Add the text fields and labels to the right section
        HBox textBoxes2 = new HBox(new HBox(10, label2, textBox2));
        HBox textBoxes1 = new HBox(new HBox(10, label1, textBox1));
        HBox textBoxes3 = new HBox(new HBox(10, label3, textBox3));
        HBox textBoxes4 = new HBox(new HBox(10, label4, textBox4));
        HBox textBoxes5 = new HBox(new HBox(10, label5, textBox5));

        textBoxes1.setPadding(new Insets(10, 10, 10, 10));
        textBoxes2.setPadding(new Insets(10, 10, 10, 10));
        textBoxes3.setPadding(new Insets(10, 10, 10, 10));
        textBoxes4.setPadding(new Insets(10, 10, 10, 10));
        textBoxes5.setPadding(new Insets(10, 10, 10, 10));

        rightSection.getChildren().addAll(
                textValeur2,
                textBoxes1,
                textBoxes2,
                textBoxes3,
                textBoxes4,
                textBoxes5,
                envoyer
        );
        // Set the background color of the pane
        leftSection.setStyle("-fx-background-color: #e3e1cd;");
        charger.setAlignment(Pos.CENTER);
        leftSection.setPadding(new Insets(40, 30, 10, 40));
        rightSection.setStyle("-fx-background-color: #e3e1cd;");
        rightSection.setPadding(new Insets(40, 30, 10, 40));
        // Add the sections to the split pane
        splitPane.getItems().addAll(leftSection, rightSection);

        // Set the split pane as the center of the border pane
        this.setCenter(splitPane);

    }

    public void display(Alert a) {

        a.show();
    }

    public Button getChargerButton() {
        return this.charger;
    }

    public TextField getTextBox1() {
        return this.textBox1;
    }

    public TextField getTextBox2() {
        return this.textBox2;
    }

    public TextField getTextBox3() {
        return this.textBox3;
    }

    public TextField getTextBox4() {
        return this.textBox4;
    }

    public TextField getTextBox5() {
        return this.textBox5;
    }

    public Button getEnvoyerButton() {
        return this.envoyer;

    }

    public void displayError(String errorMessage) {


    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Error");
    alert.setContentText(errorMessage);
    alert.showAndWait();
}

    public void displayInfoError(String infoError) {


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Message");
        alert.setContentText(infoError);
        alert.showAndWait();
    }

}
