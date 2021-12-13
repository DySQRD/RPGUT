package Jeu;

import com.google.gson.annotations.Expose;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

/**
 * 
 * @author Marc Sanchez
 *
 */

public class Tileset{

    @Expose
    //Chemin vers le .png du tileset
    public String image;

    @Expose
    //Nombre de colonne du tileset (8)
    public int columns;

    @Expose
    //Taille de l'image en pixels
    public int imageheight;
    @Expose
    public int imagewidth;

    @Expose
    //Taille de la marge et de l'espacement entre les tiles en pixels (1)
    public int margin;
    @Expose
    public int spacing;

    @Expose
    //Nombre totalde tiles (48)
    public int tilecount;

    @Expose
    //Taille des tiles en pixel (32)
    public int tileheight;
    @Expose
    public int tilewidth;

    //L'image du tileset en pixels
    public BufferedImage bufferedImage;
    
    /**
     * Permet de créer un Tileset, or l'objet qui représentera visuel un level
     */
    public Tileset(){}

    //.png -> BufferedImage
    /**
     * Récupère les textures nécessaires pour créer le level.
     * @throws IOException
     * Erreur In/Out
     */
    public void loadbImage() throws IOException {
        BufferedImage bImage = ImageIO.read(new File(this.image));
        this.bufferedImage = bImage;
    }




}
