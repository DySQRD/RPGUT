package Jeu;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Complète le travail de la classe Tileset.<br>
 * L'utilisation de la librairie GSON est obligatoire.
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
