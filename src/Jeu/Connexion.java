package Jeu;

import BD.BD;
import Exceptions.ImprevuDBError;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class Connexion extends AnimationTimer {
    protected LoopManager loopManager;

    protected HBox unHBox = new HBox();
    protected HBox pwHBox = new HBox();
    protected HBox butHBox = new HBox();
    protected VBox vBox = new VBox();
    protected Label username = new Label("Username");
    protected Label password = new Label("Password");
    protected TextField unField = new TextField();
    protected PasswordField pField = new PasswordField();
    protected Button loginButton = new Button("Jouer");
    protected Button registerButton = new Button("S'inscrire");

    protected Label message = new Label();


    protected Group root;

    public Connexion(Group root){
        this.root = root;

        unHBox.getChildren().addAll(username, unField);
        pwHBox.getChildren().addAll(password, pField);
        butHBox.getChildren().addAll(loginButton, registerButton);
        vBox.getChildren().addAll(unHBox, pwHBox, butHBox);
        message.setStyle("-fx-text-fill: red;");
    }

    public void displayUpdate(){
        root.getChildren().clear();
        root.getChildren().add(vBox);
    }
    public void displayRemove(){
        root.getChildren().clear();
    }

    public void register(String username, String password) throws ImprevuDBError, SQLException, IOException {
        switch(BD.inscrire(username, password)){
            case 0: {
                message.setText("Le joueur " + username + " existe déjà !");
                if(!(vBox.getChildren().contains(message))) vBox.getChildren().add(message);
                break;
            }
            case -1: {
                message.setText("Pseudo ou mot de passe non conforme !");
                if(!(vBox.getChildren().contains(message))) vBox.getChildren().add(message);
                break;
            }
            case 1: {
                loopManager.newGame();
                loopManager.game();
            }
        }

    }

    public void connect(String username, String password) throws ImprevuDBError, SQLException, IOException {
        switch(BD.identifier(username, password)){
            case 0: {
                message.setText("Le joueur " + username + " n'existe pas !");
                if(!(vBox.getChildren().contains(message))) vBox.getChildren().add(message);
                break;
            }
            case -1: {
                message.setText("Mot de passe incorrect !");
                if(!(vBox.getChildren().contains(message))) vBox.getChildren().add(message);
                break;
            }
            case 1: {
                loopManager.newGame();
                loopManager.game();
            }
        }
    }


    /*public void id(){
        BD.inscrire("id", "Mdp");
        BD.identifier("id", "Mdp");
    }*/



    @Override
    public void handle(long l) {

    }
}
