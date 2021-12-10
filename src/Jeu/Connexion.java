package Jeu;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Connexion extends AnimationTimer {
    protected LoopManager loopManager;

    protected HBox hBox = new HBox();
    protected Label username = new Label("Username");
    protected Label password = new Label("Password");
    protected TextField unField = new TextField();
    protected PasswordField pField = new PasswordField();
    protected Button validate = new Button("Valider");


    protected Group root;

    public Connexion(Group root){
        this.root = root;
        hBox.getChildren().addAll(username, unField, password, pField, validate);
    }

    public void displayUpdate(){
        root.getChildren().clear();
        root.getChildren().add(hBox);
    }
    public void displayRemove(){
        root.getChildren().clear();
    }


    /*public void id(){
        BD.inscrire("id", "Mdp");
        BD.identifier("id", "Mdp");
    }*/



    @Override
    public void handle(long l) {

    }
}
