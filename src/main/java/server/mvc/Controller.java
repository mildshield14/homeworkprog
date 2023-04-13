package server.mvc;

import server.models.Course;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Cette classe lie le modele avec la vue.
 *
 */
public class Controller {

    private Model modele;
    private Vue vue;


    public Vue getVue() {
        return vue;
    }

    public Controller(Model m, Vue v) {
        this.modele = m;
        this.vue = v;
        
        //detecte quand on clique sur charger; verifie si une sessiona  ete selectionnee        
        this.vue.getChargerButton().setOnAction((action) -> {
            try {
                ArrayList<Course> en= new ArrayList<Course>();
                if (this.vue.getSession()!=""){
                    
                    String com=this.vue.getSession();
                    this.modele.charger(com);
                    
                    en=this.modele.getEntries();
                    this.vue.setEntries(en);
                    
                    
                    this.vue.displayCours(en);}
                
                else{
                    this.modele.setError("You need to load the courses first... Choose a session");
                    this.vue.displayError(this.modele.getError());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        this.vue.getEnvoyerButton().setOnAction((action) -> {
            this.envoyer();
            String value1=vue.getprenom().getText();
            String value2=vue.getnom().getText();
            String value3=vue.getemail().getText();
            String value4=vue.getmatricule().getText();
            String code=modele.getCode();

            modele.updateData(value1,value2,value3,value4,code);

        });

        this.vue.getTable().setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Des qu'on clique sur un cell su tableau; automatiquement on le detecte et on le stocke

                Course selectedCourse = (Course) this.vue.getTable().getSelectionModel().getSelectedItem();
                
                if (selectedCourse != null) {
                    String code = selectedCourse.getCode();
                    this.modele.setCode(code);
                    
                    String name = selectedCourse.getName();
                    String session = selectedCourse.getSession();
                    
                    System.out.println("Selected Course: " + code + " " + name + " " + session);
                }
            }
        });



    }


private void envoyer() {
        String value1 = vue.getprenom().getText();
        String value2 = vue.getnom().getText();
        String value3 = vue.getemail().getText();
        String value4 = vue.getmatricule().getText();
        String value5 = modele.getCode();
        modele.updateData(new String(value1), new String(value2), new String(value3), new String(value4), new String(value5));

        this.modele.envoyer();

        if (this.modele.getError()!=""){
            this.vue.displayError(this.modele.getError());}else{
            modele.setCode("");
        }

        if (this.modele.getInfoError()!=""){
            this.vue.displayInfoError(this.modele.getInfoError());
        }

    }

    
}
