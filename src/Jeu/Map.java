package Jeu;

import com.google.gson.annotations.Expose;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


public class Map {
    private int spawnX;
    private int spawnY;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private ArrayList<Mob> mobs = new ArrayList<>();

    @Expose
    //Layers de la map (texture + objets)
    public ArrayList<Layer> layers;

    @Expose
    //Nombre de tiles par ligne/colonne
    public int height;
    @Expose
    public int width;

    @Expose
    //Nombre de pixels par tile (32)
    public int tileheight;
    @Expose
    public int tilewidth;

    //Tileset
    public Tileset tileset;

    //Liste des textures découpées
    public ArrayList<BufferedImage> imageTiles = new ArrayList<>();

    //Rendu image
    public BufferedImage imageRendered;

    //Canvas
    private Canvas canvas = new Canvas();

    private GraphicsContext context;

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

    public void loadCanvas(){
        this.canvas = new Canvas(this.tilewidth * this.width, this.tileheight * this.height);
        this.context = canvas.getGraphicsContext2D();
        context.drawImage(SwingFXUtils.toFXImage(imageRendered, null), 0, 0);
    }

    public Canvas getCanvas(){
        return canvas;
    }

    public int getSpawnX() {
        return spawnX;
    }
    public int getSpawnY(){
        return spawnY;
    }

    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    public void setObstacles(){
        for(int i=0; i<this.layers.get(1).objects.size(); i++){
            this.obstacles.add(this.layers.get(1).objects.get(i));
            this.obstacles.get(i).setHitbox();
        }
    }
    public ArrayList<Obstacle> getObstacles(){
        return obstacles;
    }

    public void spawnMobs(int nbreMobs){
        double spawnX;
        double spawnY;
        boolean collision = false;
        for(int i=0; i<nbreMobs; i++) {
            spawnX = Math.random() * width * tilewidth;
            spawnY = Math.random() * height * tileheight;
            this.mobs.add(new MathsMinion());
            mobs.get(i).hitbox = new Rectangle(0, 0, mobs.get(i).imageV.getFitWidth(), mobs.get(i).imageV.getFitHeight());
            mobs.get(i).tp(spawnX, spawnY);
            do {
                for (int j = 0; j < this.layers.get(1).objects.size(); j++) {
                    if (mobs.get(i).hitbox.getBoundsInParent().intersects(layers.get(1).objects.get(j).hitbox.getBoundsInParent())) {
                        spawnX = Math.random() * width * tilewidth;
                        spawnY = Math.random() * height * tileheight;
                        mobs.get(i).tp(spawnX, spawnY);
                        collision = true;break;
                    }
                    else collision = false;
                }
            } while (collision == true);
        }

    }
    public ArrayList<Mob> getMobs(){
        return mobs;
    }
}
