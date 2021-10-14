package Jeu;

import com.google.gson.Gson;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Map {

    //Layers de la map (texture + objets)
    public ArrayList<Layer> layers;

    //Nombre de tiles par ligne/colonne
    public int height;
    public int width;

    //Nombre de pixels par tile (32)
    public int tileheight;
    public int tilewidth;

    //Tileset
    public Tileset tileset;

    //Liste des textures découpées
    public ArrayList<BufferedImage> imageTiles = new ArrayList<>();

    //Rendu image
    public BufferedImage imageRendered;

    public Map(){
    }


    //Ajout du Tileset
    public void addTileset(Tileset tileset){
        this.tileset = tileset;
    }

    //Remplissement de la Liste de tiles
    public void loadTiles(){
        int coordX, coordY, coordXp, coordYp;
        for(int i=0; i<this.layers.get(0).data.size(); i++){
            coordX = ((this.layers.get(0).data.get(i) - 1 ) % this.tileset.columns );
            coordY = ((this.layers.get(0).data.get(i) - 1 ) / this.tileset.columns);

            coordXp = coordX * this.tilewidth + coordX + 1;
            coordYp = coordY * this.tileheight + coordY + 1;

            this.imageTiles.add(this.tileset.bufferedImage.getSubimage(coordXp, coordYp, this.tilewidth, this.tileheight));
        }
    }

    //Rendu image
    public void renderMap(){
        this.imageRendered = new BufferedImage(this.tilewidth * this.width, this.tileheight * this.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = this.imageRendered.createGraphics();
        for(int i=0; i<this.layers.get(0).data.size(); i++){
            graphics2D.drawImage(this.imageTiles.get(i),null, i%this.width * this.tilewidth, i/this.width * this.tileheight);
        }
    }
}
