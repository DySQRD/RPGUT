package Jeu;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 
 * @author Marc SANCHEZ
 *
 */
public class Layer {

    @Expose
    //Liste des numéros de tiles
    public ArrayList<Integer> data;

    @Expose
    //Nombre de tiles par ligne/colonne
    public int height;
    public int width;

    @Expose
    //Nom et type du layer (tile ou object)
    public String name;
    public String type;

    @Expose
    //Objets du Layer
    public ArrayList<Obstacle> objects;

    public Layer(){}

}
