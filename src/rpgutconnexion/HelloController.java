package com.example.rpgutconnexion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.xml.crypto.Data;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    Label etat;
    @FXML
    Button connexionButton;
    @FXML
    PasswordField pwField;
    @FXML
    TextField pseudoField;
    @FXML
    Button inscriptionButton;

    public void connexionOnAction(ActionEvent event){
        if(pseudoField.getText().isBlank() || pwField.getText().isBlank()) etat.setText("Veuillez remplir tous les champs.");
        else login();
    }

    public void inscriptionOnAction(ActionEvent event){
        if(pseudoField.getText().isBlank() || pwField.getText().isBlank()) etat.setText("Veuillez remplir tous les champs.");
        else inscription();
    }

    public void login(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM rpgut.utilisateur WHERE pseudo = '"+ pseudoField.getText() +"' AND password = '"+ pwField.getText()+"'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1)==1) etat.setText("Connexion réussi. Bon jeu !");
                else etat.setText("Connexion invalide. Veuillez ressayer");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inscription(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyInscription = "INSERT INTO rpgut.utilisateur(pseudo,password) VALUES('"+pseudoField.getText()+"','"+pwField.getText()+"'');";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyInscription);

            while(queryResult.next()){
                if(queryResult.getBoolean(1)==true) etat.setText("Inscription réussi !");
                else etat.setText("Ce compte existe déjà.");
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}