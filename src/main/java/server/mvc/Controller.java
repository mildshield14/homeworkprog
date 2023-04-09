package server.mvc;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/*
 * Cette classe lie le modele avec la vue.
 * À noter qu'elle ne connaît ni de detailles d'implementation
 * du comportement, ni de detailles de structuration du GUI.
 *
 */
public class Controller {

    private Model modele;
    private Vue vue;

    public Controller(Model m, Vue v) {
        this.modele = m;
        this.vue = v;

        /*
         * La definition du comportement de chaque handler
         * est mise dans sa propre méthode auxiliaire. Il pourrait être même
         * dans sa propre classe entière: ne niveau de decouplage
         * depend de la complexité de l'application
         */

        this.vue.getChargerButton().setOnAction((action) -> {
            this.charger();
        });

        this.vue.getEnvoyerButton().setOnAction((action) -> {
            this.envoyer();
            String value1=vue.getTextBox1().getText();
            String value2=vue.getTextBox2().getText();
            String value3=vue.getTextBox3().getText();
            String value4=vue.getTextBox4().getText();
            String value5=vue.getTextBox5().getText();

            modele.updateData(value1,value2,value3,value4,value5);

        });



    }

    private void charger() {
        this.modele.charger();

    }

    private void envoyer() {
        String value1 = vue.getTextBox1().getText();
        String value2 = vue.getTextBox2().getText();
        String value3 = vue.getTextBox3().getText();
        String value4 = vue.getTextBox4().getText();
        String value5 = vue.getTextBox5().getText();

        modele.updateData(new String(value1), new String(value2), new String(value3), new String(value4), new String(value5));

        this.modele.envoyer();

        if (this.modele.getError()!="")
        {        this.vue.displayError(this.modele.getError());}

        if (this.modele.getInfoError()!=""){
            this.vue.displayInfoError(this.modele.getInfoError());
        }

    }

    private void textBox1(){
        this.modele.getValue1();

    }
    private void textBox4(){
        this.modele.getValue4();


    }
    private void textBox5(){
        this.modele.getValue5();


    }

    private void textBox2(){
        this.modele.getValue2();

    }
    private void textBox3(){
        this.modele.getValue3();

    }

private void error(Alert a){
    String errorMessage = this.modele.getError();
    if (!errorMessage.isEmpty()) {
        this.vue.displayError(errorMessage);
    }
}

}
