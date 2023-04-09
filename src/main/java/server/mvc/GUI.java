package server;
package mvc;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Model leModele = new Model();
        Vue laVue = new Vue();
        Controller leControleur = new Controller(leModele, laVue);

        Scene scene = new Scene(laVue, 700, 700);

        stage.setScene(scene);
        stage.setTitle("Interface Graphique");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

