package Jeu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class Tileset{

    //Chemin vers le .png du tileset
    public String image;

    //Nombre de colonne du tileset (8)
    public int columns;

    //Taille de l'image en pixels
    public int imageheight;
    public int imagewidth;

    //Taille de la marge et de l'espacement entre les tiles en pixels (1)
    public int margin;
    public int spacing;

    //Nombre totalde tiles (48)
    public int tilecount;

    //Taille des tiles en pixel (32)
    public int tileheight;
    public int tilewidth;

    //L'image du tileset en pixels
    public BufferedImage bufferedImage;

    public Tileset(){}

    //.png -> BufferedImage
    public void loadbImage() throws IOException {
        BufferedImage bImage = ImageIO.read(new File(this.image));
        this.bufferedImage = bImage;
    }




}
