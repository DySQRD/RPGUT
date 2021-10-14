package Jeu;

import java.util.ArrayList;

public class Layer {

    //Liste des numéros de tiles
    public ArrayList<Integer> data;

    //Nombre de tiles par ligne/colonne
    public int height;
    public int width;

    //Nom et type du layer (tile ou object)
    public String name;
    public String type;

    //Objets du Layer
    public ArrayList<Obstacle> objects;

    public Layer(){}

}
