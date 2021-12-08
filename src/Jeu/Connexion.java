package Jeu;

import javafx.animation.AnimationTimer;

public class Connexion extends AnimationTimer {

    public void id(){
        BD.inscrire("id", "Mdp");
        BD.identifier("id", "Mdp");
    }



    @Override
    public void handle(long l) {

    }
}
