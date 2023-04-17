package server.mvc;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
* Met en place l'interface graphique
*/
public class GUI extends Application {
    
    /**
    * Démarre l'interface
    * @throws Exception
    */
    @Override
    public void start(Stage stage) throws Exception {
        Model leModele = new Model();
        Vue laVue = new Vue();
        Controller leControleur = new Controller(leModele, laVue);

        Scene scene = new Scene(laVue, 833, 700);

        stage.setScene(scene);
        stage.setTitle("Interface Graphique");
        stage.show();
    }
    
    /**
    * Lance l'application
    * @param args
    */
    public static void main(String[] args) {
        launch(args);
    }

}

